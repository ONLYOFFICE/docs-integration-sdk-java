package com.onlyoffice.model.commandservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandResponse {
    private Error error;
    private String key;
    private JSONObject license;
    private JSONObject server;
    private JSONObject quota;
    private String version;

    @AllArgsConstructor
    @Getter
    public enum Error implements com.onlyoffice.model.common.Error {
        NO(0, "No errors"),
        KEY(1, "Document key is missing or no document with such key could be found"),
        CALLBACK_URL(2, "Callback url not correct"),
        INTERNAL_SERVER(3, "Internal server error"),
        FORCESAVE(4, "No changes were applied to the document before the forcesave command was received"),
        COMMAND(5, "Command not correct"),
        TOKEN(6, "Invalid token");

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
