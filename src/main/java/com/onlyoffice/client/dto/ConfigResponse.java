/**
 *
 * (c) Copyright Ascensio System SIA 2026
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

package com.onlyoffice.client.dto;

import lombok.Data;

import java.util.List;


/**
 * The body of a response from {@code /meta/config}.
 */
@Data
public class ConfigResponse {
    /**
     * Where the server expects a token. Set {@code header} and {@code prefix} of the
     * {@link com.onlyoffice.model.settings.security.Security} of the client to it.
     */
    private Authorization authorization;

    /**
     * The paths of its endpoints.
     */
    private Urls urls;

    /**
     * The bounds the server enforces.
     */
    private Limits limits;

    /**
     * Language tags the editor interface is translated into, such as {@code "pt-PT"}.
     */
    private List<String> langs;

    /**
     * Where the document server expects the JWT of a request.
     */
    @Data
    public static class Authorization {
        /**
         * Name of the header carrying the token, such as {@code "Authorization"}.
         */
        private String header;

        /**
         * Prefix the token is written behind, such as {@code "Bearer "}.
         */
        private String prefix;
    }

    /**
     * Paths of the endpoints the document server serves, relative to its base URL.
     */
    @Data
    public static class Urls {
        /**
         * Script that loads the editor API, such as {@code "/web-apps/apps/api/documents/api.js"}.
         */
        private String api;

        /**
         * The command service, {@code "/command"}.
         */
        private String command;

        /**
         * The conversion service, {@code "/converter"}.
         */
        private String converter;

        /**
         * The document builder service, {@code "/docbuilder"}.
         */
        private String docbuilder;
    }

    /**
     * Bounds the document server enforces.
     */
    @Data
    public static class Limits {
        /**
         * Largest file it accepts, in bytes.
         */
        private Long maxFileSize;
    }
}
