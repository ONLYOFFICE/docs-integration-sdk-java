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

package com.onlyoffice.model.documenteditor.config.editorconfig.customization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Changes the image file at the top left corner of the editor header.
 * The recommended image height is 20 pixels.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Logo {

    /**
     * Defines the path to the image file used to show in the common work mode
     * (i.e. in view and edit modes for all editors) or in the embedded mode
     * (see the {@link com.onlyoffice.model.documenteditor.Config} section to find out how to define the
     * "embedded" document type).
     * The image must have the following size: 172 x 40.
     */
    private String image;

    /**
     * Defines the path to the image file used for the dark theme.
     * The image must have the following size: 172 x 40.
     */
    private String imageDark;

    /**
     * Defines the path to the image file used to show in the embedded mode
     * (see the {@link com.onlyoffice.model.documenteditor.Config} section to find out how to define the embedded
     * document type). The image must have the following size: 248 x 40.
     * Deprecated since version 7.0, please use the "image" field instead.
     */
    @Deprecated
    private String imageEmbedded;

    /**
     * Defines the absolute URL which will be used when someone clicks the logo image
     * (can be used to go to your web site, etc.).
     * Leave as an empty string or null to make the logo not clickable.
     */
    private String url;
}
