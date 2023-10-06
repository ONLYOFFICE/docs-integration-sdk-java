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

import com.onlyoffice.model.service.Service;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

public interface RequestManager {

    /**
     * Execute a GET request to the document server If the internal address of the document server was specified
     * in the settings, the address will be replaced with an internal one.
     *
     * @param <R> the type of the result
     * @param url the url address to the document editing server
     * @param callback the callback method
     * @return result of execution callback method
     */
    <R> R executeGetRequest(String url, Callback<R> callback) throws Exception;

    /**
     * Execute a POST request to the specified service, authorization data and the address of the document editing
     * service are taken from the SettingsManager.
     *
     * @param <R> the type of the result
     * @param service the url address to the document editing server
     * @param data the json data
     * @param callback the callback method
     * @see com.onlyoffice.manager.settings.SettingsManager
     * @see Service
     * @return result of execution callback method
     */
    <R> R executePostRequest(Service service, JSONObject data, Callback<R> callback) throws Exception;

    /**
     * Execute a POST request to the specified service.
     *
     * @param <R> the type of the result
     * @param service the url address to the document editing server
     * @param data the json data
     * @param url the url address where the document server is located
     * @param secretKey  the secret authorization key
     * @param jwtHeader  the authorization header
     * @param jwtPrefix the authorization prefix
     * @param callback the callback method
     * @see Service
     * @return result of execution callback method
     */
    <R> R executePostRequest(Service service, JSONObject data, String url, String secretKey, String jwtHeader,
                             String jwtPrefix, Callback<R> callback) throws Exception;

    /**
     * Execute a POST request to the specified service with the {@link HttpUriRequest} parameter.
     *
     * @param <R> the type of the result
     * @param service the url address to the document editing server
     * @param request the {@link HttpUriRequest} request
     * @param callback the callback method
     * @see Service
     * @return result of execution callback method
     */
    <R> R executeRequest(Service service, HttpUriRequest request, Callback<R> callback)
            throws Exception;
    interface Callback<Result> {

        /**
         * Callback method. Implement this method depending on your needs.
         *
         * @param httpEntity the type of the result
         * @return result of execution callback method
         */
        Result doWork(HttpEntity httpEntity) throws Exception;
    }
}
