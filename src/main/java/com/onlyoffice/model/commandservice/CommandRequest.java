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

package com.onlyoffice.model.commandservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.commandservice.commandrequest.Command;
import com.onlyoffice.model.commandservice.commandrequest.Meta;
import com.onlyoffice.model.common.RequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Defines the request parameters that are sent to the "https://documentserver/command"
 * address where "documentserver" is the name of the server with the ONLYOFFICE Document Server installed.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandRequest implements RequestEntity {

    /**
     * Defines the command type.
     */
    private Command c;

    /**
     * Defines the document identifier used to unambiguously identify the document file.
     */
    private String key;

    /**
     * Defines the list of the user identifiers.
     */
    private List<String> users;

    /**
     * Defines some custom identifier which will help distinguish the specific request
     * in case there were more than one.
     */
    private String userdata;

    /**
     * Defines the new meta information of the document.
     */
    private Meta meta;

    /**
     * Defines the encrypted signature added to the Document Server config in the form of a token.
     */
    private String token;

    /**
     * Returns a token from the command request.
     *
     * @return The token from the command request.
     */
    @Override
    public String getToken() {
        return this.token;
    }

    /**
     * Adds a token to the command request.
     *
     * @param tn The token.
     */
    @Override
    public void setToken(final String tn) {
        this.token = tn;
    }
}
