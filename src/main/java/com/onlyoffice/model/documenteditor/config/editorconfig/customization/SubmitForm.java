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
 * Starting from version 8.3, defines the Complete &amp; Submit button settings. If this parameter is a boolean value,
 * then it specifies whether the Complete &amp; Submit button will be displayed or hidden on the top toolbar.
 * Button will only be available for the pdf format. The default value is true.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitForm {
    /**
     * Defines whether the Complete &amp; Submit button will be displayed or hidden on the top toolbar.
     * Button will only be available for the pdf format. The default value is true.
     */
    private Boolean visible;

    /**
     * Defines a message displayed after forms are submitted. The following values are available:
     * "" - the message will not be displayed;
     * null / undefined - the default message will be displayed;
     * "text" - any text that the user specifies will be displayed.
     */
    private String resultMessage;
}
