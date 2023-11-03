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

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.service.Service;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

public interface RequestManager {

    /**
     * Executes a GET request to the document server. If the internal address of the document server was specified
     * in the settings, the address will be replaced with this URL.
     *
     * @param <R> The result type.
     * @param url The URL address to the document editing server.
     * @param callback The callback method.
     * @return The result of the execution callback method.
     */
    <R> R executeGetRequest(String url, Callback<R> callback) throws Exception;

    /**
     * Executes a POST request to the specified service. The authorization data and the address of the document editing
     * service are taken from {@link SettingsManager}.
     *
     * @param <R> The result type.
     * @param service The URL address to the document editing server.
     * @param data The JSON data.
     * @param callback The callback method.
     * @see com.onlyoffice.manager.settings.SettingsManager
     * @see Service
     * @return The result of the execution callback method.
     */
    <R> R executePostRequest(Service service, JSONObject data, Callback<R> callback) throws Exception;

    /**
     * Executes a POST request to the specified service. The authorization data and the address of the document editing
     * service are passed in the method.
     *
     * @param <R> The result type.
     * @param service The URL address to the document editing server.
     * @param data The JSON data.
     * @param url The URL address where the document server is located.
     * @param secretKey  The secret authorization key.
     * @param jwtHeader  The authorization header.
     * @param jwtPrefix The authorization prefix.
     * @param callback The callback method.
     * @see Service
     * @return The result of the execution callback method.
     */
    <R> R executePostRequest(Service service, JSONObject data, String url, String secretKey, String jwtHeader,
                             String jwtPrefix, Callback<R> callback) throws Exception;

    /**
     * Executes a POST request to the specified service with the {@link HttpUriRequest} parameter.
     *
     * @param <R> The result type.
     * @param service The URL address to the document editing server.
     * @param request The {@link HttpUriRequest} request.
     * @param callback The callback method.
     * @see Service
     * @return The result of the execution callback method.
     */
    <R> R executeRequest(Service service, HttpUriRequest request, Callback<R> callback)
            throws Exception;
    interface Callback<Result> {

        /**
         * The callback method. Implement this method depending on your needs.
         *
         * @param httpEntity The result type.
         * @return The result of the execution callback method.
         */
        Result doWork(HttpEntity httpEntity) throws Exception;
    }
}
