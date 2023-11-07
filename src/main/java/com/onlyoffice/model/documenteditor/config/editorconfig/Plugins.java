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

package com.onlyoffice.model.documenteditor.config.editorconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Defines the parameters to connect the special add-ons to your Document Server installation
 * which will help you add additional features to document editors.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Plugins {
    /**
     * Defines an array of the identifiers (as entered in "config.json") for the plugins,
     * which will automatically start when the editor opens, and the order the plugins will run one-by-one.
     * 
     * @see <a target="_top" href="https://api.onlyoffice.com/plugin/config">"config.json" in API ONLYOFFICE</a>
     */
    private List<String> autostart;

    /**
     * Defines an array of absolute URLs to the plugin configuration files ("config.json").
     * 
     * @see <a target="_top" href="https://api.onlyoffice.com/plugin/config">"config.json" in API ONLYOFFICE</a>
     */
    private List<String> pluginsData;
}
