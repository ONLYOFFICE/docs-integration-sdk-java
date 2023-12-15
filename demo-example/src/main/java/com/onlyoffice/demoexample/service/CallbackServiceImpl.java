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

package com.onlyoffice.demoexample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.Callback;
import com.onlyoffice.service.documenteditor.callback.DefaultCallbackService;
import org.springframework.stereotype.Component;

@Component
public class CallbackServiceImpl extends DefaultCallbackService {

    private final ObjectMapper mapper = new ObjectMapper();

    public CallbackServiceImpl(final JwtManager jwtManager, final SettingsManager settingsManager) {
        super(jwtManager, settingsManager);
    }


    @Override
    public void handlerEditing(final Callback callback, final String fileId) throws Exception {
        System.out.println("Editing...");
        System.out.println("Callback object: " + mapper.writeValueAsString(callback));
    }

    @Override
    public void handlerSave(final Callback callback, final String fileId) throws Exception {
        System.out.println("Saving...");
        System.out.println("Callback object: " + mapper.writeValueAsString(callback));
    }

}
