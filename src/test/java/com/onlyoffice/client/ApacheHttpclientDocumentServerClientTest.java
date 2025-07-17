/**
 *
 * (c) Copyright Ascensio System SIA 2025
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

package com.onlyoffice.client;

import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.convertservice.ConvertResponse;
import com.onlyoffice.model.properties.docsintegrationsdk.DocumentServerProperties;
import com.onlyoffice.model.settings.Settings;
import com.onlyoffice.model.settings.security.Security;
import com.onlyoffice.utils.ConfigurationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ApacheHttpclientDocumentServerClientTest {
    private DocumentServerClient documentServerClientWithSettings;
    private DocumentServerClient documentServerClientWithManagers;

    @BeforeEach
    void setup() {
        DocumentServerProperties demoDocumentServerProperties = ConfigurationUtils.getDemoDocumentServerProperties();

        this.documentServerClientWithSettings = new ApacheHttpclientDocumentServerClient(
                DocumentServerClientSettings.builder()
                        .baseUrl(demoDocumentServerProperties.getUrl())
                        .ignoreSSLCertificate(false)
                        .security(Security.builder()
                                .key(demoDocumentServerProperties.getSecurity().getKey())
                                .header(demoDocumentServerProperties.getSecurity().getHeader())
                                .prefix(demoDocumentServerProperties.getSecurity().getPrefix())
                                .build()
                        )
                        .build()
        );

        SettingsManager settingsManager = new DefaultSettingsManager() {
            @Override
            public String getSetting(final String name) {
                return "";
            }

            @Override
            public void setSetting(final String name, final String value) {

            }
        };

        UrlManager urlManager = new DefaultUrlManager(settingsManager);

        this.documentServerClientWithManagers = new ApacheHttpclientDocumentServerClient(
                settingsManager,
                urlManager
        );
    }


    @Test
    public void documentServerClientWithSettings_whenHealthcheck_thenReturnTrue() {
        Boolean healthcheck = documentServerClientWithSettings.healthcheck();
        Assertions.assertTrue(healthcheck);
    }

    @Test
    public void documentServerClientWithManagers_whenHealthcheck_thenReturnDocumentServerResponseException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            documentServerClientWithManagers.healthcheck();
        });
    }

    @Test
    public void documentServerClientWithManagers_whenApplyDemoAndHealthcheck_thenReturnTrue() {
        documentServerClientWithManagers.applySettings(Settings.builder()
                .demo(true)
                .build());

        Boolean healthcheck = documentServerClientWithSettings.healthcheck();
        Assertions.assertTrue(healthcheck);
    }

    @Test
    public void whenGetFile_thenReturnSize() {
        this.documentServerClientWithSettings.applySettings(
                DocumentServerClientSettings.builder()
                        .baseUrl("https://d2nlctn12v279m.cloudfront.net")
                        .ignoreSSLCertificate(false)
                        .build()
        );

        File tmpFile = null;

        try {
            tmpFile = Files.createTempFile("", "").toFile();
            int size = documentServerClientWithSettings.getFile(
                    "/assets/docs/samples/demo.docx",
                    Files.newOutputStream(tmpFile.toPath())
            );

            Assertions.assertTrue(size > 0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        }
    }

    @Test
    public void whenConvert_thenReturnConvertResponse() throws IOException {
        ConvertRequest convertRequest = ConvertRequest.builder()
                .key(String.valueOf(new Date().getTime()))
                .url("https://d2nlctn12v279m.cloudfront.net/assets/docs/samples/demo.docx")
                .outputtype("pdf")
                .build();

        ConvertResponse convertResponse = documentServerClientWithSettings.convert(convertRequest);

        Assertions.assertEquals("pdf", convertResponse.getFileType());
        Assertions.assertEquals(true, convertResponse.getEndConvert());
        Assertions.assertEquals(100, convertResponse.getPercent());
    }

    @Test
    public void documentServerClientWithManagers_whenConvert_thenReturnTrue() {
        documentServerClientWithManagers.applySettings(Settings.builder()
                .demo(true)
                .build());

        ConvertRequest convertRequest = ConvertRequest.builder()
                .key(String.valueOf(new Date().getTime()))
                .url("https://d2nlctn12v279m.cloudfront.net/assets/docs/samples/demo.docx")
                .outputtype("pdf")
                .build();

        ConvertResponse convertResponse = documentServerClientWithSettings.convert(convertRequest);

        Assertions.assertEquals("pdf", convertResponse.getFileType());
        Assertions.assertEquals(true, convertResponse.getEndConvert());
        Assertions.assertEquals(100, convertResponse.getPercent());
    }
}
