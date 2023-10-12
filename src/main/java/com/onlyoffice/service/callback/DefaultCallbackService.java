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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.Callback;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;


@Getter(AccessLevel.PROTECTED)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultCallbackService implements CallbackService {

    private JwtManager jwtManager;
    private SettingsManager settingsManager;

    public Callback verifyCallback(final JSONObject body, final String authorizationHeader)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Callback callback = objectMapper.readValue(body.toString(), Callback.class);

        return verifyCallback(callback, authorizationHeader);
    }

    public Callback verifyCallback(final Callback callback, final String authorizationHeader)
            throws JsonProcessingException {
        if (settingsManager.isSecurityEnabled()) {
            String token = callback.getToken();
            String payload = null;
            Boolean fromHeader = false;

            if (token == null || token.isEmpty()) {
                if (authorizationHeader != null
                        && !authorizationHeader.isEmpty()
                        && authorizationHeader.startsWith(settingsManager.getSecurityPrefix())) {
                    token = authorizationHeader.substring(settingsManager.getSecurityPrefix().length());
                    fromHeader = true;
                }
            }

            if (token == null || token == "") {
                throw new SecurityException("Not found authorization token");
            }

            payload = jwtManager.verify(token);

            JSONObject callbackFromToken = new JSONObject(payload);

            if (fromHeader) {
                callbackFromToken = callbackFromToken.getJSONObject("payload");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(callbackFromToken.toString(), Callback.class);
        }

        return callback;
    }

    public void processCallback(final Callback callback, final String fileId) throws Exception {
        switch (callback.getStatus()) {
            case EDITING:
                handlerEditing(callback, fileId);
                break;
            case SAVE:
                handlerSave(callback, fileId);
                break;
            case SAVE_CORRUPTED:
                handlerSaveCorrupted(callback, fileId);
                break;
            case CLOSED:
                handlerClosed(callback, fileId);
                break;
            case FORCESAVE:
                handlerForcesave(callback, fileId);
                break;
            case FORCESAVE_CORRUPTED:
                handlerForcesaveCurrupted(callback, fileId);
                break;
            default:
                throw new RuntimeException("Callback has no status");
        }
    }

    public void handlerEditing(final Callback callback, final String fileId) throws Exception {

    }

    public void handlerSave(final Callback callback, final String fileId) throws Exception {

    }

    public void handlerSaveCorrupted(final Callback callback, final String fileId) throws Exception {
        handlerSave(callback, fileId);
    }

    public void handlerClosed(final Callback callback, final String fileId) throws Exception {

    }

    public void handlerForcesave(final Callback callback, final String fileId) throws Exception {

    }

    public void handlerForcesaveCurrupted(final Callback callback, final String fileId) throws Exception {
        handlerForcesave(callback, fileId);
    }

}
