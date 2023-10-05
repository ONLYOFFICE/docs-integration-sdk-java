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

@AllArgsConstructor
@Getter
public enum ConvertServiceErrorCode implements ErrorCode {
    UNKNOWN_ERROR(-1, "Unknown error"),
    TIMEOUT_ERROR(-2, "Conversion timeout error"),
    CONVERSION_ERROR(-3, "Conversion error"),
    DOWNLOADING_ERROR(-4, "Error while downloading the document file to be converted"),
    PASSWORD_ERROR(-5, "Incorrect password"),
    DATABASE_ERROR(-6, "Error while accessing the conversion result database"),
    INPUT_ERROR(-7, "Input error"),
    TOKEN_ERROR(-8, "Invalid token"),
    DOWNLOAD_RESULT_ERROR(-98, "Download result file error"),
    CONNECTION_ERROR(-99, "Connection error");

    private final Integer code;
    private final String description;

    private static final Map<Integer, ErrorCode> BY_CODE = new HashMap<>();

    static {
        for (ErrorCode e: values()) {
            BY_CODE.put(e.getCode(), e);
        }
    }

    public static ErrorCode valueOfCode(final Integer code) {
        return BY_CODE.get(code);
    }
}

