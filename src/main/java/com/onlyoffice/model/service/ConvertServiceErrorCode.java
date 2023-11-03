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
 * Defines the Converting Service error codes.
 */
@AllArgsConstructor
@Getter
public enum ConvertServiceErrorCode implements ErrorCode {
    /**
     * Unknown error (the error code is -1).
     */
    UNKNOWN_ERROR(-1, "Unknown error"),

    /**
     * Conversion timeout error (the error code is -2).
     */
    TIMEOUT_ERROR(-2, "Conversion timeout error"),

    /**
     * Conversion error (the error code is -3).
     */
    CONVERSION_ERROR(-3, "Conversion error"),

    /**
     * Error while downloading the document file to be converted (the error code is -4).
     */
    DOWNLOADING_ERROR(-4, "Error while downloading the document file to be converted"),

    /**
     * Incorrect password (the error code is -5).
     */
    PASSWORD_ERROR(-5, "Incorrect password"),

    /**
     * Error while accessing the conversion result database (the error code is -6).
     */
    DATABASE_ERROR(-6, "Error while accessing the conversion result database"),

    /**
     * Input error (the error code is -7).
     */
    INPUT_ERROR(-7, "Input error"),

    /**
     * Invalid token (the error code is -8).
     */
    TOKEN_ERROR(-8, "Invalid token"),

    /**
     * Download result file error (the error code is -98).
     */
    DOWNLOAD_RESULT_ERROR(-98, "Download result file error"),

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
     * Defines a map of the {@link ErrorCode} objects with the Converting Service error codes.
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

