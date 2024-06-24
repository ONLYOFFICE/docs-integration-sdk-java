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

package com.onlyoffice.model.documenteditor.config.editorconfig.customization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Defines the parameters that the user can disable or customize if possible.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Features {

    /**
     * defines if the role settings will be disabled in the pdf forms or not. If the parameter is equal to "false",
     * then the role manager is hidden and viewing the form on behalf of a specific role is disabled.
     * In this case, the Manage Roles and View Form buttons on the Forms tab and a drop-down list for setting
     * the field role in the right panel will not be displayed. The default value is "true".
     */
    private Boolean roles;

    /**
     * Defines if the spell checker is automatically switched on or off when the editor is loaded.
     * It is set as the initial spell checker value and the spell checker setting will not be hidden.
     * The default value is "true".
     */
    private Boolean spellcheck;
}
