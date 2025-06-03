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

package com.onlyoffice.model.documenteditor.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Defines the object with the document changes history.
 * The object is present when the {@link Status} value is equal to 2 or 3 only.
 * It contains the object "changes" and "serverVersion",
 * which must be sent as properties "changes" and "serverVersion" of the object sent as the argument to the
 * "refreshHistory" method.
 *
 * @see <a target="_top" href="https://api.onlyoffice.com/docs/docs-api/usage-api/methods/#refreshhistory">"refreshHistory"
 * method in API ONLYOFFICE</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class History {

    /**
     * Defines the current server version number.
     */
    private String serverVersion;

    /**
     * Defines the changes from the history object returned after saving the document.
     */
    private List<Object> changes;
}
