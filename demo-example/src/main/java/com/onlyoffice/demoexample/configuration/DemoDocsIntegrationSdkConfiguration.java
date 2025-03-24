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

package com.onlyoffice.demoexample.configuration;

import com.onlyoffice.configuration.DocsIntegrationSdkConfiguration;
import com.onlyoffice.context.DocsIntegrationSdkContext;
import com.onlyoffice.demoexample.manager.DocumentManagerImpl;
import com.onlyoffice.demoexample.manager.SettingsManagerImpl;
import com.onlyoffice.demoexample.manager.UrlMangerImpl;
import com.onlyoffice.demoexample.service.CallbackServiceImpl;
import com.onlyoffice.demoexample.service.ConfigServiceImpl;
import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.service.documenteditor.callback.CallbackService;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoDocsIntegrationSdkConfiguration implements DocsIntegrationSdkConfiguration {
    @Value("${url}")
    private String docServerUrl;
    @Value("${security.key}")
    private String securityKey;

    @Override
    public SettingsManager settingsManager() {
        return new SettingsManagerImpl(this.docServerUrl, this.securityKey);
    }

    @Override
    public DocumentManager documentManager(final SettingsManager settingsManager) {
        return new DocumentManagerImpl(settingsManager);
    }

    @Override
    public UrlManager urlManager(final SettingsManager settingsManager) {
        return new UrlMangerImpl(settingsManager);
    }

    @Override
    public CallbackService callbackService(final JwtManager jwtManager, final SettingsManager settingsManager) {
        return new CallbackServiceImpl(jwtManager, settingsManager);
    }

    @Override
    public ConfigService configService(final DocumentManager documentManager, final UrlManager urlManager,
                                       final JwtManager jwtManager, final SettingsManager settingsManager) {
        return new ConfigServiceImpl(documentManager, urlManager, jwtManager, settingsManager);
    }

    @Bean
    DocsIntegrationSdkContext docsIntegrationSdkContext() {
       return new DocsIntegrationSdkContext(this);
    }
}
