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
    <R> R executePostRequest(RequestedService requestedService, RequestEntity requestEntity, Callback<R> callback)
            throws Exception;

    <R> R executePostRequest(String url, RequestEntity requestEntity, Credentials credentials, Callback<R> callback)
            throws Exception;

    <R> R executeRequest(HttpUriRequest request, Callback<R> callback)
            throws Exception;

    HttpPost createPostRequest(String url, RequestEntity requestEntity, Credentials credentials)
            throws JsonProcessingException;

    interface Callback<Result> {
        Result doWork(HttpEntity httpEntity) throws Exception;
    }
}
