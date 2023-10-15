package com.onlyoffice.model.convertservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvertResponse {
    private Boolean endConvert;
    private Error error;
    private String fileType;
    private String fileUrl;
    private Integer percent;

    @AllArgsConstructor
    @Getter
    public enum Error implements com.onlyoffice.model.common.Error {
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

        private static final Map<Integer, Error>
                BY_CODE = new HashMap<>();

        static {
            for (Error e: values()) {
                BY_CODE.put(e.getCode(), e);
            }
        }

        public static Error valueOfCode(final Integer code) {
            return BY_CODE.get(code);
        }
    }
}
