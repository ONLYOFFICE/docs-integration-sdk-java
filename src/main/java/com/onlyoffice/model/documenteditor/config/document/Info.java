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

package com.onlyoffice.model.documenteditor.config.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.document.info.SharingSettings;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Defines the additional parameters for the document.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Info {

    /**
     * Defines the name of the document author/creator.
     * Deprecated since version 5.4, please use {@link Info#owner owner} instead.
     */
    @Deprecated
    private String author;

    /**
     * Defines the document creation date.
     * Deprecated since version 5.4, please use {@link Info#uploaded uploaded} instead.
     */
    @Deprecated
    private String created;

    /**
     * Defines the highlighting state of the "Favorite" icon.
     * When the user clicks the icon, the "onMetaChange" event is called.
     * If the parameter is undefined, the "Favorite" icon is not displayed at the editor window header.
     *
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/config/events#onMetaChange">"onMetaChange"
     * event in API ONLYOFFICE</a>
     */
    private Boolean favorite;

    /**
     * Defines the folder where the document is stored (can be empty in case the document is stored in the root folder).
     */
    private String folder;

    /**
     * Defines the name of the document owner/creator.
     */
    private String owner;

    /**
     * Defines the information about the settings which allow to share the document with other users.
     */
    private List<SharingSettings> sharingSettings;

    /**
     * Defines the document uploading date.
     */
    private String uploaded;
}
