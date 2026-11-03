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

package com.onlyoffice.manager.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.onlyoffice.exception.IncorrectOperationException;
import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.security.JwtOperation;
import com.onlyoffice.model.settings.SettingsConstants;
import com.onlyoffice.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Properties;

public class JwtManagerTest {
    private static final String KEY = "secret";

    private final Properties properties = new Properties();

    private final SettingsManager settingsManager = new DefaultSettingsManager() {
        @Override
        public String getSetting(final String name) {
            return properties.getProperty(name);
        }

        @Override
        public void setSetting(final String name, final String value) {
            properties.put(name, value);
        }
    };

    private final JwtManager jwtManager = new DefaultJwtManager(settingsManager);

    @BeforeEach
    public void setup() {
        properties.clear();
        settingsManager.setSetting(SettingsConstants.SECURITY_KEY, KEY);
    }

    @Test
    public void whenVerifyTokenForExpectedOperation_thenReturnPayload() {
        String token = SecurityUtils.createToken(Collections.singletonMap("key", "value"), KEY, JwtOperation.DOWNLOAD);

        String payload = jwtManager.verify(token, JwtOperation.DOWNLOAD);

        Assertions.assertTrue(payload.contains("\"operation\":\"download\""));
    }

    @Test
    public void whenVerifyTokenWithoutOperation_thenReturnPayload() {
        String token = SecurityUtils.createToken(Collections.singletonMap("key", "value"), KEY);

        Assertions.assertNotNull(jwtManager.verify(token, JwtOperation.DOWNLOAD));
        Assertions.assertNotNull(jwtManager.verifyToken(token, KEY, JwtOperation.CALLBACK));
    }

    @Test
    public void whenVerifyTokenForOtherOperation_thenThrowIncorrectOperationException() {
        String token = SecurityUtils.createToken(Collections.singletonMap("key", "value"), KEY, JwtOperation.CALLBACK);

        JWTVerificationException e = Assertions.assertThrows(IncorrectOperationException.class,
                () -> jwtManager.verify(token, JwtOperation.DOWNLOAD));
        Assertions.assertInstanceOf(JWTVerificationException.class, e);
        Assertions.assertThrows(IncorrectOperationException.class,
                () -> jwtManager.verifyToken(token, KEY, JwtOperation.DOWNLOAD));
    }

    @Test
    public void whenVerifyTokenWithoutExpectedOperation_thenReturnPayload() {
        String token = SecurityUtils.createToken(Collections.singletonMap("key", "value"), KEY, JwtOperation.CALLBACK);

        Assertions.assertNotNull(jwtManager.verify(token, null));
        Assertions.assertNotNull(jwtManager.verify(token));
    }

    @Test
    public void whenVerifyTokenWithOtherKey_thenThrowSignatureVerificationException() {
        String token = SecurityUtils.createToken(Collections.singletonMap("key", "value"), "other",
                JwtOperation.DOWNLOAD);

        Assertions.assertThrows(SignatureVerificationException.class,
                () -> jwtManager.verify(token, JwtOperation.DOWNLOAD));
    }
}
