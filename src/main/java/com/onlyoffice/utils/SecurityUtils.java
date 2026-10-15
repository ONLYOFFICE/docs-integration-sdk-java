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
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.exception.IncorrectOperationException;
import com.onlyoffice.model.security.JwtOperation;

import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public final class SecurityUtils {
    /** {@link ObjectMapper}, shared since it is thread-safe once configured. */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SecurityUtils() { }

    /**
     * Creates an authorization header with a JWT token.
     * @param object Object to be included in the token payload
     * @param key Secret key used to sign the token
     * @param prefix Prefix to be added to the token (e.g., "Bearer ")
     * @return Authorization header string with prefix and token
     */
    public static String createAuthorizationHeader(final Object object, final String key, final String prefix) {
        return createAuthorizationHeader(object, key, prefix, null);
    }

    /**
     * Creates an authorization header with a JWT token for the given operation.
     * The object is wrapped in the {@code payload} claim, and the {@code operation} claim is written next to it.
     * @param object Object to be included in the token payload
     * @param key Secret key used to sign the token
     * @param prefix Prefix to be added to the token (e.g., "Bearer ")
     * @param operation The request the token is for, or {@code null} to write no {@code operation} claim
     * @return Authorization header string with prefix and token
     */
    public static String createAuthorizationHeader(final Object object, final String key, final String prefix,
                                                   final JwtOperation operation) {
        Map<String, Object> data = new HashMap<>();
        data.put("payload", object);

        Map<String, ?> payloadMap = OBJECT_MAPPER.convertValue(
                data,
                new TypeReference<Map<String, ?>>() { }
        );

        String headerToken = createToken(
                payloadMap,
                key,
                operation
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
        return createToken(object, key, (JwtOperation) null);
    }

    /**
     * Creates a JWT token for the given object and operation using the provided key.
     * @param object Object to be converted to payload
     * @param key Secret key used to sign the token
     * @param operation The request the token is for, or {@code null} to write no {@code operation} claim
     * @return Signed JWT token as string
     */
    public static String createToken(final Object object, final String key, final JwtOperation operation) {
        Map<String, ?> payloadMap = OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, ?>>() { });

        return createToken(payloadMap, key, operation);
    }

    /**
     * Creates a JWT token with the provided payload and secret key.
     * @param payloadMap Map containing the payload data to be included in the token
     * @param key Secret key used to sign the token
     * @return Signed JWT token as string
     */
    public static String createToken(final Map<String, ?> payloadMap, final String key) {
        return createToken(payloadMap, key, null);
    }

    /**
     * Creates a JWT token with the provided payload, operation and secret key.
     * The {@code operation} claim is written over an {@code operation} the payload has.
     * @param payloadMap Map containing the payload data to be included in the token
     * @param key Secret key used to sign the token
     * @param operation The request the token is for, or {@code null} to write no {@code operation} claim
     * @return Signed JWT token as string
     */
    public static String createToken(final Map<String, ?> payloadMap, final String key,
                                     final JwtOperation operation) {
        Map<String, Object> claims = new HashMap<>(payloadMap);

        if (operation != null) {
            claims.put(JwtOperation.CLAIM, operation.getValue());
        }

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
                .withPayload(claims)
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

    /**
     * Checks the {@code operation} claim of a verified token.
     * A token without the claim, as older Document Server versions send, is accepted.
     * @param payload Decoded payload of the token, as returned by {@link #verifyToken(String, String)}
     * @param expected The request the token must be for, or {@code null} to skip the check
     * @throws IncorrectOperationException if the {@code operation} claim names another request
     * @throws JWTDecodeException if the payload is not a JSON object
     */
    public static void checkOperation(final String payload, final JwtOperation expected) {
        if (expected == null) {
            return;
        }

        JsonNode claims;

        try {
            claims = OBJECT_MAPPER.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new JWTDecodeException("The payload of the token is not valid JSON", e);
        }

        if (claims == null || !claims.isObject()) {
            throw new JWTDecodeException("The payload of the token is not a JSON object");
        }

        if (!claims.has(JwtOperation.CLAIM)) {
            return;
        }

        JsonNode operation = claims.get(JwtOperation.CLAIM);
        String actual = operation.isTextual() ? operation.asText() : operation.toString();

        if (!expected.getValue().equals(actual)) {
            throw new IncorrectOperationException(expected, actual);
        }
    }
}
