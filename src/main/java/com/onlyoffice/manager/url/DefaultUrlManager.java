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

import com.onlyoffice.manager.settings.SettingsManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultUrlManager implements UrlManager {
     SettingsManager settingsManager;

     public String getDocumentServerUrl() {
         if (settingsManager.isDemoActive()) {
              return sanitizeUrl(settingsManager.getSDKSetting("integration-sdk.demo.url"));
         } else {
              return sanitizeUrl(settingsManager.getSetting("document-server-url"));
         }
     }

     public String getInnerDocumentServerUrl() {
          if (settingsManager.isDemoActive()) {
               return sanitizeUrl(settingsManager.getSDKSetting("integration-sdk.demo.url"));
          } else {
               String documentServerInnerUrl = settingsManager.getSetting("document-server-inner-url");

               if (documentServerInnerUrl != null && !documentServerInnerUrl.isEmpty()) {
                    return sanitizeUrl(documentServerInnerUrl);
               } else {
                    return getDocumentServerUrl();
               }
          }
     }

     public String getDocumentServerApiUrl() {
          return getDocumentServerUrl() + settingsManager.getSDKSetting("integration-sdk.api.url");
     }

     public String sanitizeUrl(String url) {
          if (url != null) {
               return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
          } else {
               return null;
          }
     }

     public String replaceToInnerDocumentServerUrl(String url) {
          String documentServerUrl = getDocumentServerUrl();
          String innerDocumentServerUrl = getInnerDocumentServerUrl();

          if (!documentServerUrl.equals(innerDocumentServerUrl)) {
               url = url.replace(documentServerUrl, innerDocumentServerUrl);
          }

          return url;
     }

     public String getFileUrl(String fileId) {
          return null;
     }

     public String getCallbackUrl(String fileId) {
          return null;
     }

     public String getGobackUrl(String fileId) {
          return null;
     }

     public String getCreateUrl(String fileId){
          return null;
     }

     public String getTestConvertUrl() {
          return null;
     }
}