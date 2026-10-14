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

package com.onlyoffice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onlyoffice.exception.IncorrectOperationException;
import com.onlyoffice.model.security.JwtOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtilsTest {
    private static final String KEY = "secret";
    private static final String PREFIX = "Bearer ";

    @Test
    public void whenCreateTokenWithOperation_thenTokenCarriesOperation() {
        String token = SecurityUtils.createToken(Collections.singletonMap("key", "value"), KEY,
                JwtOperation.CONVERTER);

        DecodedJWT jwt = JWT.decode(token);
        Assertions.assertEquals("converter", jwt.getClaim("operation").asString());
        Assertions.assertEquals("value", jwt.getClaim("key").asString());
    }

    @Test
    public void whenCreateTokenWithOperation_thenOperationOfPayloadIsReplaced() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("operation", "download");

        String token = SecurityUtils.createToken(payload, KEY, JwtOperation.COMMAND);

        Assertions.assertEquals("command", JWT.decode(token).getClaim("operation").asString());
        Assertions.assertEquals("download", payload.get("operation"));
    }

    @Test
    public void whenCreateTokenWithoutOperation_thenTokenCarriesNoOperation() {
        String token = SecurityUtils.createToken(Collections.singletonMap("key", "value"), KEY);

        Assertions.assertTrue(JWT.decode(token).getClaim("operation").isMissing());
    }

    @Test
    public void whenCreateAuthorizationHeaderWithOperation_thenOperationIsNextToPayload() {
        String header = SecurityUtils.createAuthorizationHeader(Collections.singletonMap("key", "value"), KEY,
                PREFIX, JwtOperation.DOCBUILDER);

        Assertions.assertTrue(header.startsWith(PREFIX));

        DecodedJWT jwt = JWT.decode(header.substring(PREFIX.length()));
        Assertions.assertEquals("docbuilder", jwt.getClaim("operation").asString());
        Assertions.assertEquals("value", jwt.getClaim("payload").asMap().get("key"));
        Assertions.assertFalse(jwt.getClaim("payload").asMap().containsKey("operation"));
    }

    @Test
    public void whenCheckOperationWithSameOperation_thenPass() {
        SecurityUtils.checkOperation("{\"operation\":\"callback\"}", JwtOperation.CALLBACK);
    }

    @Test
    public void whenCheckOperationWithoutClaim_thenPass() {
        SecurityUtils.checkOperation("{\"key\":\"value\"}", JwtOperation.CALLBACK);
    }

    @Test
    public void whenCheckOperationWithoutExpectedOperation_thenPass() {
        SecurityUtils.checkOperation("{\"operation\":\"download\"}", null);
    }

    @Test
    public void whenCheckOperationWithOtherOperation_thenThrowIncorrectOperationException() {
        IncorrectOperationException e = Assertions.assertThrows(IncorrectOperationException.class,
                () -> SecurityUtils.checkOperation("{\"operation\":\"download\"}", JwtOperation.CALLBACK));

        Assertions.assertEquals(JwtOperation.CALLBACK, e.getExpected());
        Assertions.assertEquals("download", e.getActual());
    }

    @Test
    public void whenCheckOperationWithOperationInsidePayloadOnly_thenPass() {
        SecurityUtils.checkOperation("{\"payload\":{\"operation\":\"download\"}}", JwtOperation.CALLBACK);
    }

    @Test
    public void whenCheckOperationWithNonStringOperation_thenThrowIncorrectOperationException() {
        Assertions.assertThrows(IncorrectOperationException.class,
                () -> SecurityUtils.checkOperation("{\"operation\":null}", JwtOperation.CALLBACK));
    }
}
