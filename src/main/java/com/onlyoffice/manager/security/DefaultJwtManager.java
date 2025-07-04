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

package com.onlyoffice.manager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.settings.SettingsManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;


@AllArgsConstructor
public class DefaultJwtManager implements JwtManager {

    /** {@link SettingsManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private SettingsManager settingsManager;

    /** {@link ObjectMapper}. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String createToken(final Object object) {
        Map<String, ?> payloadMap = objectMapper.convertValue(object, new TypeReference<Map<String, ?>>() { });

        return createToken(payloadMap, settingsManager.getSecurityKey());
    }

    @Override
    public String createToken(final Object object, final String key) {
        Map<String, ?> payloadMap = objectMapper.convertValue(object, new TypeReference<Map<String, ?>>() { });

        return createToken(payloadMap, key);
    }

    @Override
    public String createToken(final Map<String, ?> payloadMap, final String key) {
        Algorithm algorithm = Algorithm.HMAC256(key);

        Calendar calendar = Calendar.getInstance();
        Instant issuedAt = calendar.toInstant();
        calendar.add(
                Calendar.MINUTE,
                settingsManager.getDocsIntegrationSdkProperties()
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

    @Override
    public String verify(final String token) {
        return verifyToken(token, settingsManager.getSecurityKey());
    }

    @Override
    public String verifyToken(final String token, final String key) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Base64.Decoder decoder = Base64.getUrlDecoder();

        DecodedJWT jwt = JWT.require(algorithm)
                .acceptLeeway(settingsManager.getDocsIntegrationSdkProperties()
                        .getDocumentServer()
                        .getSecurity()
                        .getLeeway())
                .build()
                .verify(token);

        return new String(decoder.decode(jwt.getPayload()));
    }
}
