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

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Defines the measurement units used on the ruler and in dialog boxes.
 * The default value is centimeters ("cm").
 */
public enum Unit {

    /**
     * Centimeters.
     */
    @JsonProperty("cm")
    CM,

    /**
     * Points.
     */
    @JsonProperty("pt")
    PT,

    /**
     * Inches.
     */
    @JsonProperty("inch")
    INCH
}
