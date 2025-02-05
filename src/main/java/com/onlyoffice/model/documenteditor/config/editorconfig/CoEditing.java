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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.editorconfig.coediting.Mode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Defines the co-editing mode ("Fast" or "Strict") and the possibility to change it.
 * This parameter is used to apply the co-editing and viewing modes.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CoEditing {

    /**
     * Defines the co-editing mode ("fast" or "strict"). The default value is "fast".
     */
    private Mode mode;

    /**
     * Defines if the co-editing mode can be changed in the editor interface or not.
     * The default value is "true".
     */
    private Boolean change;
}
