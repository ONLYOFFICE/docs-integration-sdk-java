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
 * Contains the information which will be displayed in the editor "About" section and visible to all the editor users.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

    /**
     * Defines the postal address of the company or person who gives access to the editors or the editor authors.
     */
    private String address;

    /**
     * Defines some additional information about the company or person you want the others to know.
     */
    private String info;

    /**
     * Defines the path to the image logo (there are no special recommendations for this file,
     * but it would be better if it was in the ".png" format with transparent background).
     * The image must have the following size: 432 x 70.
     */
    private String logo;

    /**
     * Defines the path to the image logo for the dark theme (there are no special recommendations for this file,
     * but it would be better if it was in ".png" format with transparent background).
     * The image must have the following size: 432 x 70.
     */
    private String logoDark;

    /**
     * Defines the email address of the company or person who gives access to the editors or the editor authors.
     */
    private String mail;

    /**
     * Defines the name of the company or person who gives access to the editors or the editor authors.
     */
    private String name;

    /**
     * Defines the phone of the company or person who gives access to the editors or the editor authors.
     */
    private String phone;

    /**
     * Defines the home website address of the above company or person.
     */
    private String www;
}
