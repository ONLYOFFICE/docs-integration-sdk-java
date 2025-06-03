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

package com.onlyoffice.context;

import com.onlyoffice.client.DocumentServerClient;
import com.onlyoffice.configuration.DocsIntegrationSdkConfiguration;
import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.service.convert.ConvertService;
import com.onlyoffice.service.documenteditor.callback.CallbackService;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import com.onlyoffice.service.settings.SettingsValidationService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocsIntegrationSdkContext {
    /**
     * Configuration object containing the managers and settings for the SDK.
     */
    private DocsIntegrationSdkConfiguration docsIntegrationSdkConfiguration;

    /**
     * Manager for handling settings and configurations.
     */
    private SettingsManager settingsManager;

    /**
     * Manager for handling URL operations.
     */
    private UrlManager urlManager;

    /**
     * Manager for handling JWT (JSON Web Token) operations.
     */
    private JwtManager jwtManager;

    /**
     * Manager for handling document operations.
     */
    private DocumentManager documentManager;

    /**
     * Client for interacting with the document server.
     */
    private DocumentServerClient documentServerClient;

    /**
     * Service for handling document configuration operations.
     */
    private ConfigService configService;

    /**
     * Service for handling callback operations from the document editor.
     */
    private CallbackService callbackService;

    /**
     * Service for handling document conversion operations.
     */
    private ConvertService convertService;

    /**
     * Service for validating settings and configurations.
     */
    private SettingsValidationService settingsValidationService;

    /**
     * Constructs a new DocsIntegrationSdkContext with the specified configuration.
     *
     * @param docsIntegrationSdkConfiguration The configuration object containing necessary managers and settings
     */
    public DocsIntegrationSdkContext(final DocsIntegrationSdkConfiguration docsIntegrationSdkConfiguration) {
        this.docsIntegrationSdkConfiguration = docsIntegrationSdkConfiguration;

        build();
    }

    /**
     * Builds and initializes all the components of the DocsIntegrationSdkContext.
     * This method is called during construction to set up managers, services, and clients.
     */
    public void build() {
        this.settingsManager = docsIntegrationSdkConfiguration.settingsManager();
        this.jwtManager = docsIntegrationSdkConfiguration.jwtManager(this.settingsManager);
        this.documentManager = docsIntegrationSdkConfiguration.documentManager(this.settingsManager);
        this.urlManager = docsIntegrationSdkConfiguration.urlManager(this.settingsManager);

        this.documentServerClient = docsIntegrationSdkConfiguration.documentServerClient(
                this.settingsManager,
                this.urlManager
        );

        this.configService = docsIntegrationSdkConfiguration.configService(
                this.documentManager,
                this.urlManager,
                this.jwtManager,
                this.settingsManager
        );

        this.convertService = docsIntegrationSdkConfiguration.convertService(
                this.documentManager,
                this.urlManager,
                this.documentServerClient
        );

        this.callbackService = docsIntegrationSdkConfiguration.callbackService(
                this.jwtManager,
                this.settingsManager
        );

        this.settingsValidationService = docsIntegrationSdkConfiguration.settingsValidationService(
                this.documentServerClient,
                this.urlManager
        );
    }
}
