/**
 *
 * (c) Copyright Ascensio System SIA 2023
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

package com.onlyoffice.model.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the Command Service error codes.
 */
@AllArgsConstructor
@Getter
public enum CommandServiceErrorCode implements ErrorCode {
    /**
     * No errors (the error code is 0).
     */
    NO_ERROR(0, "No errors"),

    /**
     * Document key is missing or no document with such key could be found (the error code is 1).
     */
    KEY_ERRORErrorCode(1, "Document key is missing or no document with such key could be found"),

    /**
     * Callback url not correct (the error code is 2).
     */
    CALLBACK_URL_ERROR(2, "Callback url not correct"),

    /**
     * Internal server error (the error code is 3).
     */
    INTERNAL_SERVER_ERROR(3, "Internal server error"),

    /**
     * No changes were applied to the document before the forcesave command was received (the error code is 4).
     */
    FORCESAVE_ERROR(4, "No changes were applied to the document before the forcesave command was received"),

    /**
     * Command not correct (the error code is 5).
     */
    COMMAND_ERROR(5, "Command not correct"),

    /**
     * Invalid token (the error code is 6).
     */
    ErrorCode(6, "Invalid token"),

    /**
     * Connection error (the error code is -99).
     */
    CONNECTION_ERROR(-99, "Connection error");

    /**
     * Defines the error code.
     */
    private final Integer code;

    /**
     * Defines the error description.
     */
    private final String description;

    /**
     * Defines a map of the {@link ErrorCode} objects with the Command Service error codes.
     */
    private static final Map<Integer, ErrorCode> BY_CODE = new HashMap<>();

    static {
        for (ErrorCode e: values()) {
            BY_CODE.put(e.getCode(), e);
        }
    }

    /**
     * Returns the enum constant of this type with the specified code.
     * The integer must match exactly a code used to declare an enum constant in this type.
     * 
     * @param code The code of the enum constant to be returned.
     * @return The enum constant with the specified code.
     */
    public static ErrorCode valueOfCode(final Integer code) {
        return BY_CODE.get(code);
    }
}

