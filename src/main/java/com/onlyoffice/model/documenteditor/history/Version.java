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

package com.onlyoffice.model.documenteditor.history;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.common.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Defines the array with the document versions.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Version {

    /**
     * Defines the changes from the history object returned after saving the document.
     */
    private List<Object> changes;

    /**
     * Defines the document version creation date.
     */
    private String created;

    /**
     * Defines the unique document identifier used by the service to recognize the document.
     */
    private String key;

    /**
     * Defines the current server version number.
     * If the changes parameter is sent, then the "serverVersion" parameter
     * is required to be sent as well.
     */
    private String serverVersion;

    /**
     * Defines the user who is the author of the document version.
     */
    private User user;

    /**
     * Defines the document version number.
     */
    private String version;
}
