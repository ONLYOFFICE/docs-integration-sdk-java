/**
 *
 * (c) Copyright Ascensio System SIA 2025
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

package com.onlyoffice.model.docbuilderservice;

import com.fasterxml.jackson.annotation.JsonCreator;
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
public class DocBuilderResponse {
    /**
     * Defines if the generation is completed or not.
     */
    private Boolean end;

    /**
     * Defines an error occurred during the generation.
     */
    private Error error;

    /**
     * Defines the document identifier used to unambiguously identify the document file.
     */
    private String key;

    /**
     * Defines the links to the generated document.
     * This parameter will be received only when the "end" parameter is set to "true".
     */
    private Map<String, String> urls;



    @AllArgsConstructor
    @Getter
    public enum Error implements com.onlyoffice.model.common.Error {

        /**
         * Unknown error (the error code is -1).
         */
        UNKNOWN(-1, "Unknown error"),

        /**
         * Generation timeout error (the error code is -2).
         */
        TIMEOUT(-2, "Generation timeout error"),

        /**
         * Document generation error (the error code is -3).
         */
        GENERATION(-3, "Document generation error"),

        /**
         * Error while downloading the document file to be generated (the error code is -4).
         */
        DOWNLOADING(-4, "Error while downloading the document file to be generated"),

        /**
         * Error while accessing the document generation result database (the error code is -6).
         */
        DATABASE(-6, "Error while accessing the document generation result database"),

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
         * Defines a map of the {@link DocBuilderResponse.Error} objects with the DocBuilder Service error codes.
         */
        private static final Map<Integer, DocBuilderResponse.Error>
                BY_CODE = new HashMap<>();

        static {
            for (DocBuilderResponse.Error e: values()) {
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
        @JsonCreator
        public static DocBuilderResponse.Error valueOfCode(final Integer code) {
            return BY_CODE.get(code);
        }
    }
}
