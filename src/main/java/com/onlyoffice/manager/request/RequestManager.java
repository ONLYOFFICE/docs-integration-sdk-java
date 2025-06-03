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

package com.onlyoffice.manager.request;

import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.model.settings.HttpClientSettings;
import com.onlyoffice.model.settings.security.Security;

@Deprecated
public interface RequestManager {

    /**
     * Executes a GET request to the url.
     * The URL to the service is passed in the method.
     * The authorization data is taken from {@link com.onlyoffice.manager.settings.SettingsManager}.
     *
     * @param <R> The result type.
     * @param url The URL address to the document server.
     * @param callback The callback method.
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    <R> R executeGetRequest(String url, Callback<R> callback)
            throws Exception;

    /**
     * Executes a GET request to the url.
     * The URL to the service is passed in the method.
     * The authorization data is passed in the method as the "security" parameter.
     *
     * @param <R> The result type.
     * @param url The URL address to the document server.
     * @param httpClientSettings The settings for http client.
     * @param callback The callback method.
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    <R> R executeGetRequest(String url, HttpClientSettings httpClientSettings, Callback<R> callback)
            throws Exception;

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
    @Deprecated
    <R> R executePostRequest(RequestedService requestedService, RequestEntity requestEntity, Callback<R> callback)
            throws Exception;

    /**
     * Executes a POST request to the url.
     * The URL to the service is passed in the method.
     * The authorization data is passed in the method as the "security" parameter.
     *
     * @param <R> The result type.
     * @param requestedService The requested service.
     * @param requestEntity The requested entity.
     * @param httpClientSettings The settings for http client.
     * @param callback The callback method.
     * @see RequestEntity
     * @see Security
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    <R> R executePostRequest(RequestedService requestedService, RequestEntity requestEntity,
                             HttpClientSettings httpClientSettings, Callback<R> callback) throws Exception;

    /**
     * Executes a POST request to the url.
     * The URL to the service is passed in the method.
     * The authorization data is passed in the method as the "security" parameter.
     *
     * @param <R> The result type.
     * @param url The URL address to the document server.
     * @param requestEntity The requested entity.
     * @param security The security parameters.
     * @param httpClientSettings The settings for http client.
     * @param callback The callback method.
     * @see RequestEntity
     * @see Security
     * @return The result of the execution callback method.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    <R> R executePostRequest(String url, RequestEntity requestEntity, Security security,
                             HttpClientSettings httpClientSettings, Callback<R> callback) throws Exception;

    @Deprecated
    interface Callback<Result> {

        /**
         * The callback method. Implement this method depending on your needs.
         *
         * @param response The response object.
         * @return The result of the execution callback method.
         * @throws Exception If the processing fails unexpectedly.
         */
        @Deprecated
        Result doWork(Object response) throws Exception;
    }
}
