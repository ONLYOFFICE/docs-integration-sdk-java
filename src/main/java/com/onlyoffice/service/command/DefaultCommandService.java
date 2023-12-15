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

package com.onlyoffice.service.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.model.commandservice.CommandRequest;
import com.onlyoffice.model.commandservice.CommandResponse;
import com.onlyoffice.model.common.RequestedService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

import java.io.IOException;

@AllArgsConstructor
public class DefaultCommandService implements CommandService, RequestedService {
    /** {@link RequestManager}. */
    @Getter(AccessLevel.PROTECTED)
    private final RequestManager requestManager;

    /** {@link ObjectMapper}. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CommandResponse processCommand(final CommandRequest commandRequest, final String fileId) throws Exception {
        return requestManager.executePostRequest(this, commandRequest,
                new RequestManager.Callback<CommandResponse>() {
                    public CommandResponse doWork(final Object response) throws IOException {
                        String content = IOUtils.toString(((HttpEntity) response).getContent(), "utf-8");

                        CommandResponse commandResponse = objectMapper.readValue(content.toString(),
                                CommandResponse.class);

                        return commandResponse;
                    }
                });
    }
}
