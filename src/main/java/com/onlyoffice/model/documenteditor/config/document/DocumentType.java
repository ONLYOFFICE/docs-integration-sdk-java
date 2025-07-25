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

package com.onlyoffice.model.documenteditor.config.document;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Defines the document type to be opened.
 */
public enum DocumentType {

    /**
     * Text document.
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>.
     */
    @JsonProperty("word")
    WORD,

    /**
     * Spreadsheet.
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>.
     */
    @JsonProperty("cell")
    CELL,

    /**
     * Presentation.
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>.
     */
    @JsonProperty("slide")
    SLIDE,

    /**
     * PDF.
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>.
     */
    @JsonProperty("pdf")
    PDF,

    /**
     * DIAGRAM.
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>.
     */
    @JsonProperty("diagram")
    DIAGRAM
}
