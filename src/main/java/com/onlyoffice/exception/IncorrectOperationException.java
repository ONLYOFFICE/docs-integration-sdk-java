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

package com.onlyoffice.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.onlyoffice.model.security.JwtOperation;

/**
 * Thrown when the {@code operation} claim of a token names another request than the expected one.
 */
public class IncorrectOperationException extends JWTVerificationException {
    /**
     * The expected operation.
     */
    private final JwtOperation expected;

    /**
     * The value of the {@code operation} claim of the token.
     */
    private final String actual;

    /**
     * Constructs a new IncorrectOperationException.
     *
     * @param expected The expected operation.
     * @param actual The value of the {@code operation} claim of the token.
     */
    public IncorrectOperationException(final JwtOperation expected, final String actual) {
        super("The token is for '" + actual + "', expected '" + expected.getValue() + "'");

        this.expected = expected;
        this.actual = actual;
    }

    /**
     * Returns the expected operation.
     *
     * @return The expected operation.
     */
    public JwtOperation getExpected() {
        return expected;
    }

    /**
     * Returns the value of the {@code operation} claim of the token.
     *
     * @return The value of the claim.
     */
    public String getActual() {
        return actual;
    }
}
