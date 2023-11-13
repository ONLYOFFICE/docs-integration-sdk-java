package com.onlyoffice.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the response parameters that are received from the Document Server.
 */
public class CommonResponse {
    /**
     * Defines the Document Server error codes.
     */
    @AllArgsConstructor
    @Getter
    public enum Error implements com.onlyoffice.model.common.Error {
        /**
         * Healthcheck return false (the error code is -1).
         */
        HEALTHCHECK(-1, "Healthcheck return false"),

        /**
         * Download result file error (the error code is -98).
         */
        DOWNLOAD_RESULT(-98, "Download result file error"),

        /**
         * Connection error (the error code is -99).
         */
        CONNECTION(-99, "Connection error");

        /**
         * Defines the error code.
         */
        private final Integer code;

        /**
         * Defines the error description.
         */
        private final String description;

        /**
         * Defines a map of the {@link Error} objects with the common error codes.
         */
        private static final Map<Integer, CommonResponse.Error>
                BY_CODE = new HashMap<>();

        static {
            for (CommonResponse.Error e: values()) {
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
        public static CommonResponse.Error valueOfCode(final Integer code) {
            return BY_CODE.get(code);
        }
    }
}
