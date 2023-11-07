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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.callback.Action;
import com.onlyoffice.model.documenteditor.callback.ForcesaveType;
import com.onlyoffice.model.documenteditor.callback.History;
import com.onlyoffice.model.documenteditor.callback.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Defines the callback handler parameters.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Callback {
    /**
     * Defines the object received when the user takes an action with the document.
     */
    private List<Action> actions;

    /**
     * Defines the link to the file with the document editing data used to track and display the document changes history.
     * The link is present when the {@link Status} value is equal to 2 or 3 only.
     * The file must be saved and its address must be sent as "changesUrl" parameter using the "setHistoryData" method
     * to show the changes corresponding to the specific document version.
     * 
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/methods#setHistoryData">"setHistoryData" method in API ONLYOFFICE</a>
     */
    private String changesurl;

    /**
     * Defines an extension of the document that is downloaded from the link specified with the {@link Callback#url url} parameter.
     * The file type is OOXML by default but if the "assemblyFormatAsOrigin" server setting is enabled,
     * the file will be saved in its original format.
     * 
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/save#assemblyFormatAsOrigin">"assemblyFormatAsOrigin" server setting in API ONLYOFFICE</a>
     */
    private String filetype;

    /**
     * Defines the type of initiator when the force saving request is performed.
     * The type is present when the {@link Status} value is equal to 6 or 7 only.
     */
    private ForcesaveType forcesavetype;

    /**
     * Defines the object with the document changes history.
     * The object is present when the {@link Status} value is equal to 2 or 3 only.
     * It contains the object "changes" and "serverVersion",
     * which must be sent as properties "changes" and "serverVersion" of the object sent as the argument to the "refreshHistory" method.
     * 
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/methods#refreshHistory">"refreshHistory" method in API ONLYOFFICE</a>
     */
    private History history;

    /**
     * Defines the edited document identifier.
     */
    private String key;

    /**
     * Defines the status of the document.
     */
    private Status status;

    /**
     * Defines the link to the edited document to be saved with the document storage service.
     * The link is present when the {@link Status} value is equal to 2, 3, 6 or 7 only.
     */
    private String url;

    /**
     * Defines a list of the identifiers of the users who opened the document for editing.
     * When the document has been changed, the "users" will return the identifier of the user
     * who was the last to edit the document (for responses with the {@link Status} value equal to 2 or 6).
     */
    private List<String> users;

    /**
     * Defines the encrypted signature in the form of a token.
     */
    private String token;
}
