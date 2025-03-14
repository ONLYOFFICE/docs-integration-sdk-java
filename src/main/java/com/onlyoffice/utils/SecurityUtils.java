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

package com.onlyoffice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public final class SecurityUtils {
    private SecurityUtils() { }

    /**
     * Creates an authorization header with a JWT token.
     * @param object Object to be included in the token payload
     * @param key Secret key used to sign the token
     * @param prefix Prefix to be added to the token (e.g., "Bearer ")
     * @return Authorization header string with prefix and token
     */
    public static String createAuthorizationHeader(final Object object, final String key, final String prefix) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("payload", object);

        Map<String, ?> payloadMap = objectMapper.convertValue(
                data,
                new TypeReference<Map<String, ?>>() { }
        );

        String headerToken = createToken(
                payloadMap,
                key
        );

        return prefix + headerToken;
    }

    /**
     * Creates a JWT token for the given object using the provided key.
     * @param object Object to be converted to payload
     * @param key Secret key used to sign the token
     * @return Signed JWT token as string
     */
    public static String createToken(final Object object, final String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, ?> payloadMap = objectMapper.convertValue(object, new TypeReference<Map<String, ?>>() { });

        return createToken(payloadMap, key);
    }

    /**
     * Creates a JWT token with the provided payload and secret key.
     * @param payloadMap Map containing the payload data to be included in the token
     * @param key Secret key used to sign the token
     * @return Signed JWT token as string
     */
    public static String createToken(final Map<String, ?> payloadMap, final String key) {
        Algorithm algorithm = Algorithm.HMAC256(key);

        Calendar calendar = Calendar.getInstance();
        Instant issuedAt = calendar.toInstant();
        calendar.add(
                Calendar.MINUTE,
                ConfigurationUtils.getDocsIntegrationSdkProperties()
                        .getDocumentServer()
                        .getSecurity()
                        .getTokenValidityInMinutes()
        );
        Instant expiresAt = calendar.toInstant();

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withPayload(payloadMap)
                .sign(algorithm);
    }

    /**
     * Verifies a JWT token's signature and validity using the provided key.
     * @param token JWT token to verify
     * @param key Secret key used to verify the token
     * @return Decoded payload as string
     */
    public static String verifyToken(final String token, final String key) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Base64.Decoder decoder = Base64.getUrlDecoder();

        DecodedJWT jwt = JWT.require(algorithm)
                .acceptLeeway(ConfigurationUtils.getDocsIntegrationSdkProperties()
                        .getDocumentServer()
                        .getSecurity()
                        .getLeeway())
                .build()
                .verify(token);

        return new String(decoder.decode(jwt.getPayload()));
    }
}
