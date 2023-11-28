/**
 *
 * (c) Copyright Ascensio System SIA 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.onlyoffice.service.config;

import com.onlyoffice.manager.document.DefaultDocumentManager;
import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.DefaultJwtManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.documenteditor.Config;
import com.onlyoffice.model.documenteditor.config.document.DocumentType;
import com.onlyoffice.model.documenteditor.config.document.Permissions;
import com.onlyoffice.model.documenteditor.config.document.Type;
import com.onlyoffice.model.documenteditor.config.editorconfig.Mode;
import com.onlyoffice.model.settings.SettingsConstants;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import com.onlyoffice.service.documenteditor.config.DefaultConfigService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static com.onlyoffice.service.config.ConfigServiceDataTest.USER_AGENT_DESKTOP;
import static com.onlyoffice.service.config.ConfigServiceDataTest.USER_AGENT_MOBILE;

@ExtendWith(MockitoExtension.class)
public class ConfigServiceTest {
    private static Properties properties;

    @InjectMocks
    private SettingsManager settingsManager = new DefaultSettingsManager() {
        @Override
        public String getSetting(final String name) {
            return properties.getProperty(name);
        }

        @Override
        public void setSetting(final String name, final String value) {
            properties.put(name, value);
        }
    };

    @InjectMocks
    private DocumentManager documentManager = new DefaultDocumentManager(settingsManager) {
        @Override
        public String getDocumentKey(final String fileId, final boolean embedded) {
            return fileId + "_" + embedded;
        }

        @Override
        public String getDocumentName(final String fileId) {
            return "sample.docx";
        }
    };

    @InjectMocks
    private UrlManager urlManager = new DefaultUrlManager(settingsManager) {
        @Override
        public String getCallbackUrl(final String fileId) {
            return "https://www.server.com/file/12";
        }
    };

    @InjectMocks
    private JwtManager jwtManager = new DefaultJwtManager(settingsManager);

    @InjectMocks
    private ConfigService configService = new DefaultConfigService(documentManager, urlManager, jwtManager,
            settingsManager) {
        @Override
        public Permissions getPermissions(final String fileId) {
            return Permissions.builder()
                    .edit(true)
                    .build();
        }
    };

    @BeforeEach
    public void setup() {
        properties = new Properties();

        settingsManager.setSetting(SettingsConstants.SECURITY_KEY, "secret");
    }

    @Test
    void verifyCallbackTest() {
        Config config = configService.createConfig("test_file_id", Mode.EDIT, Type.DESKTOP);

        Assertions.assertEquals("docx", config.getDocument().getFileType());
        Assertions.assertEquals("test_file_id_false", config.getDocument().getKey());
        Assertions.assertEquals(DocumentType.WORD, config.getDocumentType());
        Assertions.assertEquals(true, config.getToken() != null);
        Assertions.assertEquals("https://www.server.com/file/12", config.getEditorConfig().getCallbackUrl());
    }


    @Test
    void verifyCallbackModeViewTest() {
        Config config = configService.createConfig("test_file_id", Mode.VIEW, Type.DESKTOP);

        Assertions.assertEquals(null, config.getEditorConfig().getCallbackUrl());
    }

    @Test
    void verifyCallbackWithUserAgentTest() {
        Config config = configService.createConfig(
                "test_file_id",
                Mode.EDIT,
                USER_AGENT_DESKTOP
        );

        Assertions.assertEquals(Type.DESKTOP, config.getType());

        Config config2 = configService.createConfig(
                "test_file_id",
                Mode.EDIT,
                USER_AGENT_MOBILE
        );

        Assertions.assertEquals(Type.MOBILE, config2.getType());
    }

}
