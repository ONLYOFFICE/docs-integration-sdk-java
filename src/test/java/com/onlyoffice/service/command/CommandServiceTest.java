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

package com.onlyoffice.service.command;

import com.onlyoffice.manager.request.DefaultRequestManager;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.security.DefaultJwtManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.commandservice.CommandRequest;
import com.onlyoffice.model.commandservice.CommandResponse;
import com.onlyoffice.model.commandservice.commandrequest.Command;
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.model.settings.SettingsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@Disabled
public class CommandServiceTest {

    private UrlManager urlManager;
    private JwtManager jwtManager;
    private SettingsManager settingsManager;
    private RequestManager requestManager;
    private CommandService commandService;
    private RequestedService requestedService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        Map<String, String> settings = new HashMap<>();
        settings.put(SettingsConstants.URL, "");
        settings.put(SettingsConstants.SECURITY_KEY, "");

        settingsManager = new DefaultSettingsManager() {
            @Override
            public String getSetting(final String name) {
                return settings.get(name);
            }

            @Override
            public void setSetting(final String name, final String value) {

            }
        };

        urlManager = new DefaultUrlManager(settingsManager);
        jwtManager = new DefaultJwtManager(settingsManager);
        requestManager = new DefaultRequestManager(urlManager, jwtManager, settingsManager);

         DefaultCommandService defaultCommandService = new DefaultCommandService(requestManager);
        commandService = defaultCommandService;
        requestedService = defaultCommandService;
    }

    @Test
    void getForgottenList() throws Exception {
        CommandRequest commandRequest = CommandRequest.builder()
                .c(Command.GET_FORGOTTEN_LIST)
                .build();

        CommandResponse commandResponse = commandService.processCommand(commandRequest, null);
        System.out.println(commandResponse.getKeys());
    }

    @Test
    void getForgottenByKey() throws Exception {
        CommandRequest commandRequest = CommandRequest.builder()
                .c(Command.GET_FORGOTTEN)
                .key("L_5gf4MJJSsm0d_DHFPipLU-sZvBciWu9R185wXv39U")
                .build();

        CommandResponse commandResponse = commandService.processCommand(commandRequest, null);
        System.out.println(commandResponse.getKey() + " : " + commandResponse.getUrl());
    }

    @Test
    void deleteForgotten() throws Exception {
        CommandRequest commandRequest = CommandRequest.builder()
                .c(Command.DELETE_FORGOTTEN)
                .key("L_5gf4MJJSsm0d_DHFPipLU-sZvBciWu9R185wXv39U")
                .build();

        CommandResponse commandResponse = commandService.processCommand(commandRequest, null);
        System.out.println(commandResponse.getKey());
    }
}
