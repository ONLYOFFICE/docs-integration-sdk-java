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

package com.onlyoffice.model.documenteditor.config.editorconfig;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Defines the editor opening mode. Can be either "view" to open the document for viewing,
 * or "edit" to open the document in the editing mode allowing to apply changes to the document data.
 * The default value is "edit".
 */
public enum Mode {

    /**
     * Opens a document for viewing.
     */
    @JsonProperty("view")
    VIEW,

    /**
     * Opens a document for editing.
     */
    @JsonProperty("edit")
    EDIT
}
