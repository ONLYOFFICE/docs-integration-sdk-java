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
import org.json.JSONObject;

public interface CallbackService {

    /**
     * Verifies the {@link Callback} object.
     *
     * @param callback  The {@link Callback} object with the callback handler parameters.
     * @param authorizationHeader The authorization header from the callback request.
     * @return The verified callback object.
     * @throws JsonProcessingException An error occurred when processing the JSON data.
     */
    Callback verifyCallback(Callback callback, String authorizationHeader) throws JsonProcessingException;

    /**
     * Starts the callback handler.
     *
     * @param callback The {@link Callback} object with the callback handler parameters.
     * @param fileId The file ID.
     * @throws Exception If the processing fails unexpectedly.
     */
    void processCallback(Callback callback, String fileId) throws Exception;

    /**
     * Starts the handler that is called if the callback status is 1 (EDITING).
     *
     * @param callback The {@link Callback} object with the callback handler parameters.
     * @param fileId The file ID.
     * @throws Exception If the processing fails unexpectedly.
     */
    void handlerEditing(Callback callback, String fileId) throws Exception;

    /**
     * Starts the handler that is called if the callback status is 2 (SAVE).
     *
     * @param callback The {@link Callback} object with the callback handler parameters.
     * @param fileId The file ID.
     * @throws Exception If the processing fails unexpectedly.
     */
    void handlerSave(Callback callback, String fileId) throws Exception;

    /**
     * Starts the handler that is called if the callback status is 3 (SAVE_CORRUPTED).
     *
     * @param callback The {@link Callback} object with the callback handler parameters.
     * @param fileId The file ID.
     * @throws Exception If the processing fails unexpectedly.
     */
    void handlerSaveCorrupted(Callback callback, String fileId) throws Exception;

    /**
     * Starts the handler that is called if the callback status is 4 (CLOSED).
     *
     * @param callback The {@link Callback} object with the callback handler parameters.
     * @param fileId The file ID.
     * @throws Exception If the processing fails unexpectedly.
     */
    void handlerClosed(Callback callback, String fileId) throws Exception;

    /**
     * Starts the handler that is called if the callback status is 6 (FORCESAVE).
     *
     * @param callback The {@link Callback} object with the callback handler parameters.
     * @param fileId The file ID.
     * @throws Exception If the processing fails unexpectedly.
     */
    void handlerForcesave(Callback callback, String fileId) throws Exception;

    /**
     * Starts the handler that is called if the callback status is 7 (FORCESAVE_CORRUPTED).
     *
     * @param callback The {@link Callback} object with the callback handler parameters.
     * @param fileId The file ID.
     * @throws Exception If the processing fails unexpectedly.
     */
    void handlerForcesaveCurrupted(Callback callback, String fileId) throws Exception;
}
