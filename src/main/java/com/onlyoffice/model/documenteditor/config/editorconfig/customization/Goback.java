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

package com.onlyoffice.model.documenteditor.config.editorconfig.customization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Defines settings for the "Open file location" menu button and upper right corner button.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Goback {

    /**
     * Opens the website in the new browser tab/window (if the value is set to "true")
     * or the current tab (if the value is set to "false") when the "Open file location" button is clicked.
     * The default value is "true".
     */
    private Boolean blank;

    /**
     * Defines that if the "Open file location" button is clicked, "events.onRequestClose" event is called
     * instead of opening a browser tab or window. The default value is "false".
     * Deprecated since version 8.1. Please use
     * the {@link com.onlyoffice.model.documenteditor.config.editorconfig.Customization#close close} parameter instead
     *
     * @see <a href="https://api.onlyoffice.com/docs/docs-api/usage-api/config/events/#onrequestclose">"onRequestClose"
     * event in API ONLYOFFICE</a>
     */
    @Deprecated
    private Boolean requestClose;

    /**
     * Defines the text which will be displayed for the "Open file location" menu button
     * and upper right corner button (i.e. instead of "Go to Documents").
     */
    private String text;

    /**
     * Defines the absolute URL to the website address which will be opened when clicking the "Open file location"
     * menu button.
     */
    private String url;
}
