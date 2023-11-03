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
 * Defines the Document Server error codes.
 */
@AllArgsConstructor
@Getter
public enum DocumentServerErrorCode implements ErrorCode {
    /**
     * Healthcheck return false (the error code is -1).
     */
    HEALTHCHECK_ERROR(-1, "Healthcheck return false"),

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
     * Defines a map of the {@link ErrorCode} objects with the Document Server error codes.
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

