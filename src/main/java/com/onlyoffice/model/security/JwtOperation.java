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

package com.onlyoffice.model.security;

import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Defines the request a token is for, the {@code operation} claim.
 * <p>
 * Requests to Document Server: {@link #CONVERTER}, {@link #COMMAND}, {@link #DOCBUILDER}.
 * Document Server rejects a token whose {@code operation} claim names another endpoint.
 * <p>
 * Requests Document Server sends: {@link #DOWNLOAD}, {@link #CALLBACK}.
 * Checking the claim keeps a token taken from one request from being replayed as the other.
 */
public enum JwtOperation {

    /**
     * A callback Document Server sends to {@code callbackUrl}.
     */
    CALLBACK("callback"),

    /**
     * A request to the {@code /command} endpoint.
     */
    COMMAND("command"),

    /**
     * A request to the {@code /converter} endpoint.
     */
    CONVERTER("converter"),

    /**
     * A request to the {@code /docbuilder} endpoint.
     */
    DOCBUILDER("docbuilder"),

    /**
     * A file download Document Server performs.
     */
    DOWNLOAD("download");

    /**
     * The name of the claim the operation is written to.
     */
    public static final String CLAIM = "operation";

    private final String value;

    JwtOperation(final String value) {
        this.value = value;
    }

    /**
     * Returns the value of the {@code operation} claim.
     *
     * @return The value of the claim.
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
