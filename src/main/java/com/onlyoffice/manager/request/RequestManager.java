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
    <R> R executeGetRequest(String url, Callback<R> callback) throws Exception;
    <R> R executePostRequest(Service service, JSONObject data, Callback<R> callback) throws Exception;
    <R> R executePostRequest(Service service, JSONObject data, String url, String secretKey, String jwtHeader,
                             String jwtPrefix, Callback<R> callback) throws Exception;
    <R> R executeRequest(Service service, HttpUriRequest request, Callback<R> callback)
            throws Exception;
    interface Callback<Result> {
        Result doWork(HttpEntity httpEntity) throws Exception;
    }
}
