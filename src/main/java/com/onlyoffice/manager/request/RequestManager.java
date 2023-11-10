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

package com.onlyoffice.manager.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.model.settings.security.Security;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

public interface RequestManager {
    /**
     * Executes a POST request to the specified service. The authorization data and the address of the document server
     * are taken from {@link com.onlyoffice.manager.settings.SettingsManager}.
     *
     * @param <R> The result type.
     * @param requestedService The requested service.
     * @param requestEntity The request entity.
     * @param callback The callback method.
     * @see com.onlyoffice.manager.settings.SettingsManager
     * @see RequestedService
     * @see RequestEntity
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    <R> R executePostRequest(RequestedService requestedService, RequestEntity requestEntity, Callback<R> callback)
            throws Exception;

    /**
     * Executes a POST request to the specified service. The authorization data and the address of the document server
     * are taken from {@link com.onlyoffice.manager.settings.SettingsManager}.
     *
     * @param <R> The result type.
     * @param url The URL address to the document server.
     * @param requestEntity The request entity.
     * @param security The security.
     * @param callback The callback method.
     * @see RequestEntity
     * @see Credentials
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    <R> R executePostRequest(String url, RequestEntity requestEntity, Security security, Callback<R> callback)
            throws Exception;


    /**
     * Execute a POST request to the specified service with the {@link HttpUriRequest} parameter.
     *
     * @param <R> The result type.
     * @param request The {@link HttpUriRequest} request.
     * @param callback The callback method.
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    <R> R executeRequest(HttpUriRequest request, Callback<R> callback)
            throws Exception;

    HttpPost createPostRequest(String url, RequestEntity requestEntity, Security security)
    /**
     * Returns jwt signed http request.
     *
     * @param url The URL address to the document server.
     * @param requestEntity The request entity.
     * @param security The security.
     * @return The jwt signed http request.
     * @throws JsonProcessingException An error occurred when processing the JSON data
     */
    HttpPost createPostRequest(String url, RequestEntity requestEntity, Security security)
            throws JsonProcessingException;

    interface Callback<Result> {

        /**
         * The callback method. Implement this method depending on your needs.
         *
         * @param httpEntity The result type.
         * @return The result of the execution callback method.
         * @throws Exception If the processing fails unexpectedly.
         */
        Result doWork(HttpEntity httpEntity) throws Exception;
    }
}
