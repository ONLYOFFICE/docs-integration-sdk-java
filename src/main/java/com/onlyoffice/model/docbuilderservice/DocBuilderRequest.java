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

package com.onlyoffice.model.docbuilderservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.common.RequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocBuilderRequest implements RequestEntity {
    /**
     * Defines the arguments to pass to the created document.
     */
    private Object argument;

    /**
     * Defines the type of the request to the document builder service: asynchronous or not.
     */

    private Boolean async;
    /**
     * Defines the request identifier used to unambiguously identify the request. The key is formed on the document
     * builder service side and is returned as the response to the first request. When the asynchronous request is used
     * (the async parameter is set to true) the key is not present in the first request, but must be present in all the
     * following requests which will be send before the generation is complete. When the synchronous request is used
     * (the async parameter is set to false), this parameter is not required.
     */
    private String key;

    /**
     * Defines the encrypted signature added to the ONLYOFFICE Docs config in the form of a token.
     */
    private String token;

    /**
     * Defines the absolute URL to the .docbuilder file.
     */
    private String url;
}
