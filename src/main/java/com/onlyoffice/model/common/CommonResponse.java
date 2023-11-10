package com.onlyoffice.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class CommonResponse {
    @AllArgsConstructor
    @Getter
    public enum Error implements com.onlyoffice.model.common.Error {
        HEALTHCHECK(-1, "Healthcheck return false"),
        DOWNLOAD_RESULT(-98, "Download result file error"),
        CONNECTION(-99, "Connection error");

        private final Integer code;
        private final String description;

        private static final Map<Integer, CommonResponse.Error>
                BY_CODE = new HashMap<>();

        static {
            for (CommonResponse.Error e: values()) {
                BY_CODE.put(e.getCode(), e);
            }
        }

        public static CommonResponse.Error valueOfCode(final Integer code) {
            return BY_CODE.get(code);
        }
    }
}
