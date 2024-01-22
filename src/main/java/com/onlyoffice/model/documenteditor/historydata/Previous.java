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

package com.onlyoffice.model.documenteditor.historydata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Defines the object of the previous version of the document
 * if {@link com.onlyoffice.model.documenteditor.Callback#changesurl changesurl} address
 * was returned after saving the document.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Previous {

    /**
     * Defines an extension of the document specified with the "previous.url" parameter.
     */
    private String fileType;

    /**
     * Defines the document identifier of the previous version of the document.
     */
    private String key;

    /**
     * Defines the url address of the previous version of the document.
     */
    private String url;
}
