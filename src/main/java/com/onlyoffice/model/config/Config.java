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

package com.onlyoffice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.config.document.Document;
import com.onlyoffice.model.config.document.documenttype.DocumentType;
import com.onlyoffice.model.config.document.type.Type;
import com.onlyoffice.model.config.editor.EditorConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the Document Server config.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Config {
    /**
     * Defines the document type to be opened.
     */
    private DocumentType documentType;

    /**
     * Defines the document height (100% by default) in the browser window.
     */
    private String height;

    /**
     * Defines the encrypted signature added to the Document Server config in the form of a token.
     */
    private String token;

    /**
     * Defines the platform type used to access the document.
     */
    private Type type;

    /**
     * Defines the document width (100% by default) in the browser window.
     */
    private String width;

    /**
     * Defines the editor interface parameters.
     */
    private EditorConfig editorConfig;

    /**
     * Defines the document parameters.
     */
    private Document document;
}
