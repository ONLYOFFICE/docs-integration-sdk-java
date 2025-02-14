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

package com.onlyoffice.model.documenteditor.config.editorconfig.customization.review;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Defines the review editing mode which will be used when the document is opened for viewing.
 * It will only be available for the document editor if
 * {@link com.onlyoffice.model.documenteditor.config.editorconfig.Mode} is set to "view". The default value is
 * "original".
 */
public enum ReviewDisplay {

    /**
     * The document is displayed with proposed changes highlighted.
     */
    @JsonProperty("markup")
    MARKUP,

    /**
     * The document is displayed with proposed changes highlighted, but the balloons are turned off.
     */
    @JsonProperty("simple")
    SIMPLE,

    /**
     * The document is displayed with all the proposed changes applied.
     */
    @JsonProperty("final")
    FINAL,

    /**
     * The original document is displayed without the proposed changes.
     */
    @JsonProperty("original")
    ORIGINAL
}

