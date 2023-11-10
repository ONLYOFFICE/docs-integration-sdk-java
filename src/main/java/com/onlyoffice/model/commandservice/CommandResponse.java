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

/**
 * Defines the response parameters that are received from the document command service.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandResponse {
    /**
     * Defines an error code.
     */
    private Error error;

    /**
     * Defines the document identifier used to unambiguously identify the document file.
     */
    private String key;

    /**
     * Defines the document license information: "end_date" - the license expiration date,
     * "trial" - defines if the license is trial or not,
     * "customization" - defines if the customization parameters marked with the * sign are available
     * for editing only in ONLYOFFICE Developer Edition or not,
     * "connections" - the number of connections for the connection license,
     * "connections_view" - the number of connections for the live viewer,
     * "users_count" - the number of users for the user license,
     * "users_view_count" - the number of users for the live viewer,
     * "users_expire" - the number of days after which the user license expires.
     */
    private JSONObject license;

    /**
     * 	Defines the server characteristics: "resultType" - the license status
     * (1 - an error occurred, 2 - the license expired, 3 - the license is still available,
     * 6 - the trial license expired), "packageType" - the product version
     * (0 - an open source product, 1 - the Enterprise Edition, 2 - the Developer Edition),
     * "buildDate" - the build date, "buildVersion" - the build version, "buildNumber" - the build number.
     */
    private JSONObject server;

    /**
     * Defines the user quota value: "users" - the user quota for the user license
     * where "userid" - the id of the user who opened the editor,
     * "expire" - date of license expiration for this user, "users_view" - the user quota for the live viewer
     * where "userid" - the id of the user who opened the editor,
     * "expire" - date of viewing expiration for this user.
     */
    private JSONObject quota;

    /**
     * Defines the document version.
     */
    private String version;

    /**
     * Defines the Command Service error codes.
     */
    @AllArgsConstructor
    @Getter
    public enum Error implements com.onlyoffice.model.common.Error {
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
         * No changes were applied to the document
         * before the forcesave command was received (the error code is 4).
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
         * Defines a map of the {@link Error} objects with the Command Service error codes.
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
