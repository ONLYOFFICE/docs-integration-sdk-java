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

package com.onlyoffice.manager.url;

import com.onlyoffice.model.common.RequestedService;


public interface UrlManager {

     /**
      * Returns the URL to the Document Server.
      *
      * @return The URL to the Document Server.
      */
     String getDocumentServerUrl();

     /**
      * Returns the inner URL to the Document Server.
      *
      * @return The inner URL to the Document Server.
      */
     String getInnerDocumentServerUrl();

     /**
      * Returns the URL to the Document Server API.
      *
      * @return The URL to the Document Server API.
      */
     String getDocumentServerApiUrl();

     /**
      * Returns the URL to the Document Server Preloader API.
      *
      * @return The URL to the Document Server Preloader API.
      */
     String getDocumentServerPreloaderApiUrl();

     /**
      * Returns the URL to download a file with the ID specified in the request.
      *
      * @param fileId The file ID.
      * @return The URL to download a file.
      */
     String getFileUrl(String fileId);

     /**
      * Returns the URL to the callback handler.
      *
      * @param fileId The file ID.
      * @return The URL to the callback handler.
      */
     String getCallbackUrl(String fileId);

     /**
      * Returns the URL to the location folder of a file with the ID specified in the request.
      *
      * @param fileId The file ID.
      * @return The URL to the file location folder.
      */
     String getGobackUrl(String fileId);

     /**
      * Returns the URL to create a new file with the ID specified in the request.
      *
      * @param fileId The file ID.
      * @return The URL to create a new file.
      */
     String getCreateUrl(String fileId);

     /**
      * Returns the URL to the service using the requested service properties specified in the request.
      *
      * @param requestedService The requested service.
      * @return The URL to ONLYOFFICE service.
      */
     String getServiceUrl(RequestedService requestedService);

     /**
      * Removes the trailing slash from the URL if it exists.
      *
      * @param url The URL to the Document Server.
      * @return The URL without the trailing slash.
      */
     String sanitizeUrl(String url);

     /**
      * Replaces the URL to the Document Server with the internal one.
      *
      * @param url The URL to the Document Server.
      * @return The internal URL.
      */
     String replaceToInnerDocumentServerUrl(String url);

     /**
      * Returns the URL to the test file.
      *
      * @param url The URL to the integration Product.
      * @return The URL to the test file.
      */
     String getTestConvertUrl(String url);
}
