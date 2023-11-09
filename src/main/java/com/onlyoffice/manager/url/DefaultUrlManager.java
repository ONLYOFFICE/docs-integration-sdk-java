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
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.model.settings.SettingsConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.MessageFormat;

@Getter(AccessLevel.PROTECTED)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultUrlManager implements UrlManager {
     private SettingsManager settingsManager;

     public String getDocumentServerUrl() {
         if (settingsManager.isDemoActive()) {
              return sanitizeUrl(settingsManager.getSDKSetting("integration-sdk.demo.url"));
         } else {
              return sanitizeUrl(settingsManager.getSetting(SettingsConstants.URL));
         }
     }

     public String getInnerDocumentServerUrl() {
          if (settingsManager.isDemoActive()) {
               return sanitizeUrl(settingsManager.getSDKSetting("integration-sdk.demo.url"));
          } else {
               String documentServerInnerUrl = settingsManager.getSetting(SettingsConstants.INNER_URL);

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

     public String getServiceUrl(final RequestedService requestedService) {
          String serviceName = requestedService
                  .getClass()
                  .getInterfaces()[0]
                  .getSimpleName()
                  .replaceAll("Service", "")
                  .toLowerCase();

          String serviceUrl = settingsManager.getSDKSetting(
                  MessageFormat.format("integration-sdk.service.{0}.url", serviceName)
          );

          return getInnerDocumentServerUrl() + serviceUrl;
     }

     @Override
     public String sanitizeUrl(final String url) {
          if (url != null) {
               return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
          } else {
               return null;
          }
     }

     public String replaceToInnerDocumentServerUrl(final String url) {
          String documentServerUrl = getDocumentServerUrl();
          String innerDocumentServerUrl = getInnerDocumentServerUrl();

          if (!documentServerUrl.equals(innerDocumentServerUrl)) {
               return url.replace(documentServerUrl, innerDocumentServerUrl);
          }

          return url;
     }

     public String getFileUrl(final String fileId) {
          return null;
     }

     public String getCallbackUrl(final String fileId) {
          return null;
     }

     public String getGobackUrl(final String fileId) {
          return null;
     }

     public String getCreateUrl(final String fileId) {
          return null;
     }

     public String getTestConvertUrl() {
          return null;
     }
}
