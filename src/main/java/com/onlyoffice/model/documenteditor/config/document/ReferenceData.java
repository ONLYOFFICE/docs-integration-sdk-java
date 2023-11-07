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
import com.onlyoffice.model.config.document.Document;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines an object that is generated by the integrator to uniquely identify a file in its system.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ReferenceData {
    /**
     * Defines the unique document identifier used by the service to get a link to the file.
     * It must not be changed when the document is edited and saved (i.e. it is not equal to the {@link Document#key document.key} parameter).
     */
    private String fileKey;

    /**
     * Defines the unique system identifier.
     * If the data was copied from a file on one system, and inserted into a file on another,
     * then pasting by link will not be available and there will be no corresponding button in the context menu.
     */
    private String instanceId;
}