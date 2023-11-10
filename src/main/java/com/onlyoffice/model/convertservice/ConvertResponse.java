package com.onlyoffice.model.convertservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the response parameters that are received from the document conversion service.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvertResponse {
    /**
     * Defines if the conversion is completed or not.
     */
    private Boolean endConvert;

    /**
     * Defines an error occurred during the conversion.
     */
    private Error error;

    /**
     * Defines an extension of the converted file.
     */
    private String fileType;

    /**
     * Defines the link to the converted document.
     * This parameter will be received only when the "endConvert" parameter is set to "true".
     */
    private String fileUrl;

    /**
     * Defines the percentage of the file conversion.
     * If the "endConvert" parameter is set to "true", the percent is equal to "100".
     */
    private Integer percent;

    /**
     * Defines the Conversion Service error codes.
     */
    @AllArgsConstructor
    @Getter
    public enum Error implements com.onlyoffice.model.common.Error {
        /**
         * Unknown error (the error code is -1).
         */
        UNKNOWN(-1, "Unknown error"),

        /**
         * Conversion timeout error (the error code is -2).
         */
        TIMEOUT(-2, "Conversion timeout error"),

        /**
         * Conversion error (the error code is -3).
         */
        CONVERSION(-3, "Conversion error"),

        /**
         * Error while downloading the document file to be converted (the error code is -4).
         */
        DOWNLOADING(-4, "Error while downloading the document file to be converted"),

        /**
         * Incorrect password (the error code is -5).
         */
        PASSWORD(-5, "Incorrect password"),

        /**
         * Error while accessing the conversion result database (the error code is -6).
         */
        DATABASE(-6, "Error while accessing the conversion result database"),

        /**
         * Input error (the error code is -7).
         */
        INPUT(-7, "Input error"),

        /**
         * Invalid token (the error code is -8).
         */
        TOKEN(-8, "Invalid token");

        /**
         * Defines the error code.
         */
        private final Integer code;

        /**
         * Defines the error description.
         */
        private final String description;

        /**
         * Defines a map of the {@link Error} objects with the Conversion Service error codes.
         */
        private static final Map<Integer, Error>
                BY_CODE = new HashMap<>();

        static {
            for (Error e: values()) {
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
        public static Error valueOfCode(final Integer code) {
            return BY_CODE.get(code);
        }
    }
}
