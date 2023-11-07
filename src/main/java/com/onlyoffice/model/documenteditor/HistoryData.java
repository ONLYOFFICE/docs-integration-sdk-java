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
 * Defines the user who is the author of the document version.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class HistoryData {
    private String changesUrl;
    private String error;
    private String fileType;
    private String key;
    private Previous previous;
    private String token;
    private String url;
    private String version;
}