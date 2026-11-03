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

import com.onlyoffice.model.security.JwtOperation;
import com.onlyoffice.utils.SecurityUtils;

import java.util.Map;


public interface JwtManager {

    /**
     * Creates a new JWT for the specified object using the key from
     * {@link com.onlyoffice.manager.settings.SettingsManager}.
     *
     * @param object The object to create JWT.
     * @see com.onlyoffice.manager.settings.SettingsManager
     * @return A new JWT token.
     */
    String createToken(Object object);

    /**
     * Creates a new JWT for the specified object.
     * The key is passed as a method parameter.
     *
     * @param object The object to create JWT.
     * @param key  The secret key value.
     * @return A new JWT token.
     */
    String createToken(Object object, String key);

    /**
     * Creates a new JWT for the specified payload map.
     * The key is passed as a method parameter.
     *
     * @param payloadMap The payload map to create JWT.
     * @param key  The secret key value.
     * @return A new JWT token.
     */
    String createToken(Map<String, ?> payloadMap, String key);

    /**
     * Performs the verification against the given token using the key from
     * {@link com.onlyoffice.manager.settings.SettingsManager}.
     *
     * @param token The token to be verified.
     * @see com.onlyoffice.manager.settings.SettingsManager
     * @return The verified and decoded payload.
     */
    String verify(String token);

    /**
     * Performs the verification against the given token
     * using the key passed as a method parameter.
     *
     * @param token The token to be verified.
     * @param key  The secret key value.
     * @return The verified and decoded payload.
     */
    String verifyToken(String token, String key);

    /**
     * Performs the verification against the given token using the key from
     * {@link com.onlyoffice.manager.settings.SettingsManager}
     * and checks that the token is for the expected request.
     * <p>
     * Document Server signs its own requests with the {@code operation} claim:
     * {@link JwtOperation#DOWNLOAD} for a file download and {@link JwtOperation#CALLBACK} for a callback.
     * A token whose {@code operation} claim is another value is refused, so a token taken
     * from one request can't be replayed as the other. A token without the claim,
     * as older Document Server versions send, is accepted.
     *
     * @param token The token to be verified.
     * @param operation The request the token must be for, or {@code null} to skip the check.
     * @see com.onlyoffice.manager.settings.SettingsManager
     * @return The verified and decoded payload.
     * @throws com.onlyoffice.exception.IncorrectOperationException if the {@code operation} claim
     * names another request.
     */
    default String verify(final String token, final JwtOperation operation) {
        String payload = verify(token);

        SecurityUtils.checkOperation(payload, operation);

        return payload;
    }

    /**
     * Performs the verification against the given token using the key passed as a method parameter
     * and checks that the token is for the expected request, as {@link #verify(String, JwtOperation)} does.
     *
     * @param token The token to be verified.
     * @param key  The secret key value.
     * @param operation The request the token must be for, or {@code null} to skip the check.
     * @return The verified and decoded payload.
     * @throws com.onlyoffice.exception.IncorrectOperationException if the {@code operation} claim
     * names another request.
     */
    default String verifyToken(final String token, final String key, final JwtOperation operation) {
        String payload = verifyToken(token, key);

        SecurityUtils.checkOperation(payload, operation);

        return payload;
    }
}
