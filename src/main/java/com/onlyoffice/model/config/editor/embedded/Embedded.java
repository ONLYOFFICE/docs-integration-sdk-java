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

package com.onlyoffice.model.config.editor.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.config.Config;
import com.onlyoffice.model.config.editor.embedded.toolbar.Toolbar;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the parameters for the embedded document type only (see the {@link Config} section to find out how to define the embedded document type).
 * It allows to change the settings which define the behavior of the buttons in the embedded mode.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Embedded {
    /**
     * Defines the absolute URL to the document serving as a source file for the document embedded into the web page.
     */
    private String embedUrl;

    /**
     * Defines the absolute URL to the document which will open in full screen mode.
     */
    private String fullscreenUrl;

    /**
     * Defines the absolute URL that will allow the document to be saved onto the user personal computer.
     */
    private String saveUrl;

    /**
     * Defines the absolute URL that will allow other users to share this document.
     */
    private String shareUrl;

    /**
     * Defines the place for the embedded viewer toolbar, can be either "top" or "bottom".
     */
    private Toolbar toolbarDocked;
}
