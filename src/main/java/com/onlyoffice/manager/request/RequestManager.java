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
import com.onlyoffice.model.security.Credentials;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

public interface RequestManager {
    /**
     * Executes a POST request to the specified service.
     * The URL to the service is taken from the "requestedService" parameter.
     * The authorization data is taken from {@link com.onlyoffice.manager.settings.SettingsManager}.
     *
     * @param <R> The result type.
     * @param requestedService The requested service.
     * @param requestEntity The requested entity.
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
     * Executes a POST request to the specified service. The URL to the service is passed in the method.
     * The authorization data is passed in the method as the "credentials" parameter.
     *
     * @param <R> The result type.
     * @param url The URL address to the document server.
     * @param requestEntity The requested entity.
     * @param credentials The credentials.
     * @param callback The callback method.
     * @see RequestEntity
     * @see Credentials
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    <R> R executePostRequest(String url, RequestEntity requestEntity, Credentials credentials, Callback<R> callback)
            throws Exception;


    /**
     * Executes a POST request to the specified service with the {@link HttpUriRequest} parameter.
     *
     * @param <R> The result type.
     * @param request The {@link HttpUriRequest} request.
     * @param callback The callback method.
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    <R> R executeRequest(HttpUriRequest request, Callback<R> callback)
            throws Exception;

    /**
     * Returns the JWT signed HTTP request.
     *
     * @param url The URL address to the document server.
     * @param requestEntity The requested entity.
     * @param credentials The credentials.
     * @return The JWT signed HTTP request.
     * @throws JsonProcessingException An error occurred when processing the JSON data.
     */
    HttpPost createPostRequest(String url, RequestEntity requestEntity, Credentials credentials)
            throws JsonProcessingException;

    interface Callback<Result> {

        /**
         * The callback method. Implement this method depending on your needs.
         *
         * @param httpEntity The HTTP entity.
         * @return The result of the execution callback method.
         * @throws Exception If the processing fails unexpectedly.
         */
        Result doWork(HttpEntity httpEntity) throws Exception;
    }
}
