/**
 *
 * (c) Copyright Ascensio System SIA 2026
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

package com.onlyoffice.service.callback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.exception.IncorrectOperationException;
import com.onlyoffice.manager.security.DefaultJwtManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.Callback;
import com.onlyoffice.model.documenteditor.callback.Status;
import com.onlyoffice.model.security.JwtOperation;
import com.onlyoffice.model.settings.SettingsConstants;
import com.onlyoffice.service.documenteditor.callback.CallbackService;
import com.onlyoffice.service.documenteditor.callback.DefaultCallbackService;
import com.onlyoffice.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Properties;

import static com.onlyoffice.service.callback.CallbackServiceDataTest.AUTHORIZATION_HEADER_STATUS_EDITING;
import static com.onlyoffice.service.callback.CallbackServiceDataTest.CALLBACK_STATUS_CLOSED;
import static com.onlyoffice.service.callback.CallbackServiceDataTest.CALLBACK_STATUS_EDITING;
import static com.onlyoffice.service.callback.CallbackServiceDataTest.CALLBACK_STATUS_EDITING_WITH_TOKEN;
import static com.onlyoffice.service.callback.CallbackServiceDataTest.CALLBACK_STATUS_FORCESAVE;
import static com.onlyoffice.service.callback.CallbackServiceDataTest.CALLBACK_STATUS_SAVE;

@ExtendWith(MockitoExtension.class)
public class CallbackServiceTest {
    private static Properties properties;
    private static Status status;

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
    private JwtManager jwtManager = new DefaultJwtManager(settingsManager);

    @InjectMocks
    private ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private CallbackService callbackService = new DefaultCallbackService(jwtManager, settingsManager) {
        @Override
        public void handlerEditing(final Callback callback, final String fileId) throws Exception {
            status = Status.EDITING;
        }

        @Override
        public void handlerSave(final Callback callback, final String fileId) throws Exception {
            status = Status.SAVE;
        }

        @Override
        public void handlerClosed(final Callback callback, final String fileId) throws Exception {
            status = Status.CLOSED;
        }

        @Override
        public void handlerForcesave(final Callback callback, final String fileId) throws Exception {
            status = Status.FORCESAVE;
        }
    };

    @BeforeEach
    public void setup() {
        properties = new Properties();
        status = null;

        settingsManager.setSetting(SettingsConstants.SECURITY_KEY, "secret");
    }

    @Test
    void verifyCallbackTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING_WITH_TOKEN, Callback.class);
        Callback verifyedCallback = callbackService.verifyCallback(callback, "");
    }

    @Test
    void verifyCallbackWithoutTokenTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING, Callback.class);
        Assertions.assertThrows(SecurityException.class, () -> callbackService.verifyCallback(callback, ""));
    }

    @Test
    void verifyCallbackWithoutTokenWithAuthorizationHeaderTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING, Callback.class);
        Callback verifyedCallback = callbackService.verifyCallback(callback, AUTHORIZATION_HEADER_STATUS_EDITING);
    }

    @Test
    void verifyCallbackWithCallbackOperationTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING, Callback.class);
        callback.setToken(SecurityUtils.createToken(callbackClaims(), "secret", JwtOperation.CALLBACK));

        Callback verifiedCallback = callbackService.verifyCallback(callback, "");
        Assertions.assertEquals(Status.EDITING, verifiedCallback.getStatus());
    }

    @Test
    void verifyCallbackWithDownloadOperationTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING, Callback.class);
        callback.setToken(SecurityUtils.createToken(callbackClaims(), "secret", JwtOperation.DOWNLOAD));

        Assertions.assertThrows(IncorrectOperationException.class,
                () -> callbackService.verifyCallback(callback, ""));
    }

    @Test
    void verifyCallbackWithCallbackOperationInAuthorizationHeaderTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING, Callback.class);
        String header = SecurityUtils.createAuthorizationHeader(callbackClaims(), "secret",
                settingsManager.getSecurityPrefix(), JwtOperation.CALLBACK);

        Callback verifiedCallback = callbackService.verifyCallback(callback, header);
        Assertions.assertEquals(Status.EDITING, verifiedCallback.getStatus());
    }

    @Test
    void verifyCallbackWithDownloadOperationInAuthorizationHeaderTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING, Callback.class);
        String header = SecurityUtils.createAuthorizationHeader(callbackClaims(), "secret",
                settingsManager.getSecurityPrefix(), JwtOperation.DOWNLOAD);

        Assertions.assertThrows(IncorrectOperationException.class,
                () -> callbackService.verifyCallback(callback, header));
    }

    private Map<String, ?> callbackClaims() throws Exception {
        return mapper.readValue(CALLBACK_STATUS_EDITING, new TypeReference<Map<String, ?>>() { });
    }

    @Test
    void processCallbackEditingTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_EDITING, Callback.class);
        callbackService.processCallback(callback, "");
        Assertions.assertEquals(status, Status.EDITING);
    }

    @Test
    void processCallbackSaveTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_SAVE, Callback.class);
        callbackService.processCallback(callback, "");
        Assertions.assertEquals(status, Status.SAVE);
    }

    @Test
    void processCallbackClosedTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_CLOSED, Callback.class);
        callbackService.processCallback(callback, "");
        Assertions.assertEquals(status, Status.CLOSED);
    }

    @Test
    void processCallbackForcsaveTest() throws Exception {
        Callback callback = mapper.readValue(CALLBACK_STATUS_FORCESAVE, Callback.class);
        callbackService.processCallback(callback, "");
        Assertions.assertEquals(status, Status.FORCESAVE);
    }
}
