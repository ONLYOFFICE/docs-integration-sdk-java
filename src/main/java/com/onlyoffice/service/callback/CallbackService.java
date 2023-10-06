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

package com.onlyoffice.service.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onlyoffice.model.callback.Callback;
import org.json.JSONObject;

public interface CallbackService {

    /**
     * Callback verification from JSON object.
     *
     * @param body the body callback request
     * @param authorizationHeader the authorization header from callback request
     * @return the verified callback object
     * @throws JsonProcessingException
     */
    Callback verifyCallback(JSONObject body, String authorizationHeader) throws JsonProcessingException;

    /**
     * Callback verification.
     *
     * @param callback  the object {@link Callback}
     * @param authorizationHeader the authorization header from callback request
     * @return the verified callback object
     * @throws JsonProcessingException
     */
    Callback verifyCallback(Callback callback, String authorizationHeader) throws JsonProcessingException;

    /**
     * Callback processing.
     *
     * @param callback the object {@link Callback}
     * @param fileId the ID of the file
     * @throws Exception
     */
    void processCallback(Callback callback, String fileId) throws Exception;

    /**
     * Handler called if callback status is 1 (EDITING).
     *
     * @param callback the object {@link Callback}
     * @param fileId the ID of the file
     * @throws Exception
     */
    void handlerEditing(Callback callback, String fileId) throws Exception;

    /**
     * Handler called if callback status is 2 (SAVE).
     *
     * @param callback the object {@link Callback}
     * @param fileId the ID of the file
     * @throws Exception
     */
    void handlerSave(Callback callback, String fileId) throws Exception;

    /**
     * Handler called if callback status is 3 (SAVE_CORRUPTED).
     *
     * @param callback the object {@link Callback}
     * @param fileId the ID of the file
     * @throws Exception
     */
    void handlerSaveCorrupted(Callback callback, String fileId) throws Exception;

    /**
     * Handler called if callback status is 4 (CLOSED).
     *
     * @param callback the object {@link Callback}
     * @param fileId the ID of the file
     * @throws Exception
     */
    void handlerClosed(Callback callback, String fileId) throws Exception;

    /**
     * Handler called if callback status is 6 (FORCESAVE).
     *
     * @param callback the object {@link Callback}
     * @param fileId the ID of the file
     * @throws Exception
     */
    void handlerForcesave(Callback callback, String fileId) throws Exception;

    /**
     * Handler called if callback status is 7 (FORCESAVE_CORRUPTED).
     *
     * @param callback the object {@link Callback}
     * @param fileId the ID of the file
     * @throws Exception
     */
    void handlerForcesaveCurrupted(Callback callback, String fileId) throws Exception;
}
