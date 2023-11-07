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

package com.onlyoffice.service.documenteditor.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onlyoffice.model.documenteditor.Callback;

public interface CallbackService {
    Callback verifyCallback(Callback callback, String authorizationHeader) throws JsonProcessingException;
    void processCallback(Callback callback, String fileId) throws Exception;
    void handlerEditing(Callback callback, String fileId) throws Exception;
    void handlerSave(Callback callback, String fileId) throws Exception;
    void handlerSaveCorrupted(Callback callback, String fileId) throws Exception;
    void handlerClosed(Callback callback, String fileId) throws Exception;
    void handlerForcesave(Callback callback, String fileId) throws Exception;
    void handlerForcesaveCurrupted(Callback callback, String fileId) throws Exception;
}
