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

package com.onlyoffice.manager.url;

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.model.properties.docsintegrationsdk.DocumentServerProperties;
import com.onlyoffice.model.settings.SettingsConstants;
import com.onlyoffice.utils.ConfigurationUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.core5.net.URIBuilder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;


@AllArgsConstructor
public class DefaultUrlManager implements UrlManager {

     /** {@link SettingsManager}. */
     @Getter(AccessLevel.PROTECTED)
     @Setter(AccessLevel.PROTECTED)
     private SettingsManager settingsManager;

     @Override
     public String getDocumentServerUrl() {
         if (settingsManager.isDemoActive()) {
              return sanitizeUrl(ConfigurationUtils.getDemoDocumentServerProperties().getUrl());
         } else {
              return sanitizeUrl(settingsManager.getSetting(SettingsConstants.URL));
         }
     }

     @Override
     public String getInnerDocumentServerUrl() {
          if (settingsManager.isDemoActive()) {
               return sanitizeUrl(ConfigurationUtils.getDemoDocumentServerProperties().getUrl());
          } else {
               String documentServerInnerUrl = settingsManager.getSetting(SettingsConstants.INNER_URL);

               if (documentServerInnerUrl != null && !documentServerInnerUrl.isEmpty()) {
                    return sanitizeUrl(documentServerInnerUrl);
               } else {
                    return getDocumentServerUrl();
               }
          }
     }

     @Override
     public String getDocumentServerApiUrl() {
          return getDocumentServerApiUrl(null);
     }

     @Override
     public String getDocumentServerApiUrl(final String shardKey) {
          try {
               URIBuilder uriBuilder = new URIBuilder(
                       getDocumentServerUrl() + settingsManager.getDocsIntegrationSdkProperties()
                       .getDocumentServer()
                       .getApiUrl()
               );

               if (shardKey != null && !shardKey.isEmpty()) {
                    uriBuilder.addParameter("shardkey", shardKey);
               }

               return uriBuilder.build().toString();
          } catch (URISyntaxException e) {
               return getDocumentServerUrl() + settingsManager.getDocsIntegrationSdkProperties()
                       .getDocumentServer()
                       .getApiUrl();
          }
     }

     @Override
     public String getDocumentServerPreloaderApiUrl() {
          return getDocumentServerUrl() + settingsManager.getDocsIntegrationSdkProperties()
                  .getDocumentServer()
                  .getApiPreloaderUrl();
     }

     @Override
     public String getServiceUrl(final RequestedService requestedService) {
          String serviceUrl = null;

          if (requestedService == null) {
               return null;
          }

          String serviceName = requestedService
                  .getClass()
                  .getInterfaces()[0]
                  .getSimpleName()
                  .toLowerCase();

          DocumentServerProperties documentServerProperties = settingsManager.getDocsIntegrationSdkProperties()
                  .getDocumentServer();

          try {
               Object serviceProperties = null;

               BeanInfo beanInfo = Introspector.getBeanInfo(documentServerProperties.getClass());
               for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                    if (propertyDescriptor.getName().toLowerCase().equals(serviceName)) {
                         serviceProperties = propertyDescriptor.getReadMethod().invoke(documentServerProperties);
                    }
               }

               if (serviceProperties == null) {
                    return null;
               }

               beanInfo = Introspector.getBeanInfo(serviceProperties.getClass());
               for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                    if (propertyDescriptor.getName().equals("url")) {
                         serviceUrl = propertyDescriptor.getReadMethod().invoke(serviceProperties).toString();
                    }
               }
          } catch (IntrospectionException e) {
               throw new RuntimeException(e);
          } catch (InvocationTargetException e) {
               throw new RuntimeException(e);
          } catch (IllegalAccessException e) {
               throw new RuntimeException(e);
          }

          if (serviceUrl == null) {
               return null;
          }

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

     @Override
     public String replaceToDocumentServerUrl(final String url) {
          String documentServerUrl = getDocumentServerUrl();
          String innerDocumentServerUrl = getInnerDocumentServerUrl();

          if (!documentServerUrl.equals(innerDocumentServerUrl)) {
               return url.replace(innerDocumentServerUrl, documentServerUrl);
          }

          return url;
     }

     @Override
     public String replaceToInnerDocumentServerUrl(final String url) {
          String documentServerUrl = getDocumentServerUrl();
          String innerDocumentServerUrl = getInnerDocumentServerUrl();

          if (!documentServerUrl.equals(innerDocumentServerUrl)) {
               return url.replace(documentServerUrl, innerDocumentServerUrl);
          }

          return url;
     }

     @Override
     public String getFileUrl(final String fileId) {
          return null;
     }

     @Override
     public String getDirectFileUrl(final String fileId) {
          return null;
     }

     @Override
     public String getCallbackUrl(final String fileId) {
          return null;
     }

     @Override
     public String getGobackUrl(final String fileId) {
          return null;
     }

     @Override
     public String getCreateUrl(final String fileId) {
          return null;
     }

     @Override
     public String getTestConvertUrl(final String url) {
          return null;
     }
}
