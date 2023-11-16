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

package com.onlyoffice.model.documenteditor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.historydata.Previous;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Defines the version history data.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class HistoryData {

    /**
     * Defines the url address of the file with the document changes data,
     * which can be downloaded by the
     * {@link com.onlyoffice.model.documenteditor.Callback#changesurl changesurl} link
     * from the JSON object returned after saving the document.
     * The request for file is signed with a token which is checked by the Document Server.
     */
    private String changesUrl;

    /**
     * Defines the error message text.
     */
    private String error;

    /**
     * Defines an extension of the document specified with the "url" parameter.
     */
    private String fileType;

    /**
     * Defines the document identifier used to unambiguously identify the document file.
     */
    private String key;

    /**
     * Defines the object of the previous version of the document
     * if {@link com.onlyoffice.model.documenteditor.Callback#changesurl changesurl} address
     * was returned after saving the document.
     */
    private Previous previous;

    /**
     * Defines the encrypted signature added to the parameter in the form of a token.
     */
    private String token;

    /**
     * Defines the url address of the current document version.
     * Can be downloaded by the {@link com.onlyoffice.model.documenteditor.Callback#url url} link
     * from the JSON object returned after saving the document.
     * Be sure to add a token when using local links. Otherwise, an error will occur.
     */
    private String url;

    /**
     * Defines the document version number.
     */
    private String version;
}
