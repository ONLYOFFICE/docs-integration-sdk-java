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

package com.onlyoffice.model.documenteditor.config.document.info.sharingsettings;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Defines the access rights for the user with the name above.
 */
public enum Permissions {

    /**
     * The user has the full access to the document.
     */
    @JsonProperty("Full Access")
    FULL_ACCESS,

    /**
     * The user has the read-only access to the document.
     */
    @JsonProperty("Read Only")
    READ_ONLY,

    /**
     * The user does not have the access to the document.
     */
    @JsonProperty("Deny Access")
    DENY_ACCESS
}
