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

public interface UrlManager {
     /**
      * Gets url to Document Server.
      *
      * @return the url to Document Server
      */
     String getDocumentServerUrl();

     /**
      * Gets inner url to Document Server.
      *
      * @return the inner url to Document Server
      */
     String getInnerDocumentServerUrl();

     /**
      * Gets url to Document Server API.
      *
      * @return the url Document Server API
      */
     String getDocumentServerApiUrl();

     /**
      * Gets url to file download.
      *
      * @param fileId the ID of the file
      * @return the url to file download
      */
     String getFileUrl(String fileId);

     /**
      * Gets url to callback handler.
      *
      * @param fileId the ID of the file
      * @return the url to callback handler
      */
     String getCallbackUrl(String fileId);

     /**
      * Gets url to file location folder.
      *
      * @param fileId the ID of the file
      * @return the url to file location folder
      */
     String getGobackUrl(String fileId);

     /**
      * Gets url to create new.
      *
      * @param fileId the ID of the file
      * @return the url to create new
      */
     String getCreateUrl(String fileId);

     /**
      * Remove trailing slash from URL if it exists.
      *
      * @param url the url to document server
      * @return the url without trailing slash
      */
     String sanitizeUrl(String url);

     /**
      * Replace url to .
      *
      * @param url the url to document server
      * @return the url without trailing slash
      */
     String replaceToInnerDocumentServerUrl(String url);

     /**
      * Gets url to test file.
      *
      * @return the url to test file
      */
     String getTestConvertUrl();
}
