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

package com.onlyoffice.demoexample.controllers;

import com.onlyoffice.context.DocsIntegrationSdkContext;
import com.onlyoffice.model.documenteditor.Callback;
import com.onlyoffice.service.documenteditor.callback.CallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ResponseBody
@RestController
public class CallbackController {

    @Autowired
    private DocsIntegrationSdkContext docsIntegrationSdk;

    @PostMapping("/callback")
    public String callback(@RequestBody final Callback callback) {
        CallbackService callbackService = docsIntegrationSdk.getCallbackService();

        try {
            Callback verifiedCallback =  callbackService.verifyCallback(callback, null);

            callbackService.processCallback(verifiedCallback, "1");
            return "{\"error\": 0}";
        } catch (Exception e) {
            return "{\"error\": 1}";
        }
    }
}
