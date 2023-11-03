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

package com.onlyoffice.model.config.document.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the platform type used to access the document.
 */
public enum Type {
    /**
     * The platform which is optimized to access the document from a desktop or laptop computer.
     */
    @JsonProperty("desktop")
    DESKTOP,

    /**
     * The platform which is optimized to access the document from a tablet or a smartphone.
     */
    @JsonProperty("mobile")
    MOBILE,

    /**
     * The platform which is specifically formed to be easily embedded into a web page.
     */
    @JsonProperty("embedded")
    EMBEDDED
}
