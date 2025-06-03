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

package com.onlyoffice.configuration;

import com.onlyoffice.client.ApacheHttpclientDocumentServerClient;
import com.onlyoffice.client.DocumentServerClient;
import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.DefaultJwtManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.service.convert.ConvertService;
import com.onlyoffice.service.convert.DefaultConvertServiceV2;
import com.onlyoffice.service.documenteditor.callback.CallbackService;
import com.onlyoffice.service.documenteditor.callback.DefaultCallbackService;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import com.onlyoffice.service.documenteditor.config.DefaultConfigService;
import com.onlyoffice.service.settings.DefaultSettingsValidationServiceV2;
import com.onlyoffice.service.settings.SettingsValidationService;

/**
 * Configuration interface for the ONLYOFFICE Docs Integration SDK.
 * Provides methods to access core components and configure the document server client.
 */
public interface DocsIntegrationSdkConfiguration {
    /**
     * Gets the settings manager instance.
     * @return The settings manager
     */
    SettingsManager settingsManager();

    /**
     * Creates a DocumentManager instance for managing document operations.
     *
     * @param settingsManager The settings manager to use for document operations
     * @return A configured DocumentManager instance
     */
    DocumentManager documentManager(SettingsManager settingsManager);

    /**
     * Creates and configures a JwtManager instance.
     *
     * @param settingsManager The settings manager used to configure the JWT manager
     * @return A configured JwtManager instance that handles JWT operations
     */
    default JwtManager jwtManager(SettingsManager settingsManager) {
        return new DefaultJwtManager(settingsManager);
    }

    /**
     * Gets the URL manager instance.
     * @param settingsManager The settings manager to use for configuration
     * @return The URL manager
     */
    default UrlManager urlManager(SettingsManager settingsManager) {
        return new DefaultUrlManager(settingsManager);
    }

    /**
     * Creates and configures a DocumentServerClient instance.
     *
     * @param settingsManager The settings manager containing configuration settings
     * @param urlManager The URL manager for handling server URLs
     * @return A configured DocumentServerClient instance
     */
    default DocumentServerClient documentServerClient(final SettingsManager settingsManager,
                                                      final UrlManager urlManager) {
        return new ApacheHttpclientDocumentServerClient(settingsManager, urlManager);
    }

    /**
     * Creates and configures a ConfigService instance.
     *
     * @param documentManager The document manager to handle document operations
     * @param urlManager The URL manager to handle URL operations
     * @param jwtManager The JWT manager to handle JSON Web Token operations
     * @param settingsManager The settings manager to handle configuration settings
     * @return A new instance of ConfigService configured with the provided managers
     */
    default ConfigService configService(DocumentManager documentManager, UrlManager urlManager, JwtManager jwtManager,
                                        SettingsManager settingsManager) {
        return new DefaultConfigService(
                documentManager,
                urlManager,
                jwtManager,
                settingsManager
        );
    }

    /**
     * Creates and configures a default CallbackService instance.
     *
     * @param jwtManager The JWT manager used for token handling and validation
     * @param settingsManager The settings manager for configuration management
     * @return A new instance of DefaultCallbackService configured with the provided managers
     */
    default CallbackService callbackService(JwtManager jwtManager, SettingsManager settingsManager) {
        return new DefaultCallbackService(jwtManager, settingsManager);
    }

    /**
     * Creates and configures a ConvertService instance.
     *
     * @param documentManager The document manager used for handling document operations
     * @param urlManager The URL manager used for managing URLs
     * @param documentServerClient The client for interacting with the document server
     * @return A new instance of ConvertService configured with the provided components
     */
    default ConvertService convertService(DocumentManager documentManager, UrlManager urlManager,
                        DocumentServerClient documentServerClient) {
        return new DefaultConvertServiceV2(documentManager, urlManager, documentServerClient);
    }

    /**
     * Creates and configures a SettingsValidationService instance.
     *
     * @param documentServerClient The client for interacting with the document server
     * @param urlManager The URL manager used for managing URLs
     * @return A new instance of SettingsValidationService configured with the provided components
     */
    default SettingsValidationService settingsValidationService(DocumentServerClient documentServerClient,
                                                                UrlManager urlManager) {
        return new DefaultSettingsValidationServiceV2(documentServerClient, urlManager) {
        };
    }
}
