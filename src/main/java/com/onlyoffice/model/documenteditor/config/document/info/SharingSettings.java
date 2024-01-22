/**
 *
 * (c) Copyright Ascensio System SIA 2024
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

package com.onlyoffice.model.documenteditor.config.document.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.document.info.sharingsettings.Permissions;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Defines the information about the settings which allow to share the document with other users.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SharingSettings {

    /**
     * Specifies whether to change the user icon to the link icon.
     */
    private Boolean isLink;

    /**
     * Defines the access rights for the user with the name above.
     */
    private Permissions permissions;

    /**
     * Defines the name of the user the document will be shared with.
     */
    private String user;
}
