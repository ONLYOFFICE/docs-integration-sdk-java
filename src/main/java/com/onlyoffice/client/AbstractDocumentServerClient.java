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

package com.onlyoffice.client;

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.properties.docsintegrationsdk.DocumentServerProperties;
import com.onlyoffice.model.properties.docsintegrationsdk.HttpClientProperties;
import com.onlyoffice.model.settings.Settings;
import com.onlyoffice.model.settings.security.Security;
import com.onlyoffice.utils.ConfigurationUtils;

import java.util.Optional;

public abstract class AbstractDocumentServerClient implements DocumentServerClient {
    /** {@link SettingsManager}. */
    protected SettingsManager settingsManager;
    /** {@link UrlManager}. */
    protected UrlManager urlManager;

    /** Document server client settings. */
    protected DocumentServerClientSettings documentServerClientSettings = new DocumentServerClientSettings();

    protected AbstractDocumentServerClient(final DocumentServerClientSettings documentServerClientSettings) {
        this.documentServerClientSettings = documentServerClientSettings;
    }

    protected AbstractDocumentServerClient(final SettingsManager settingsManager, final UrlManager urlManager) {
        this.settingsManager = settingsManager;
        this.urlManager = urlManager;
    }

    @Override
    public void applySettings(final Settings settings) {
        if (Optional.ofNullable(settings.getDemo()).orElse(false)) {
           DocumentServerProperties demoDocumentServerProperties = ConfigurationUtils.getDemoDocumentServerProperties();

            DocumentServerClientSettings documentServerClientSettings = DocumentServerClientSettings.builder()
                    .baseUrl(demoDocumentServerProperties.getUrl())
                    .ignoreSSLCertificate(false)
                    .security(Security.builder()
                            .key(demoDocumentServerProperties.getSecurity().getKey())
                            .header(demoDocumentServerProperties.getSecurity().getHeader())
                            .prefix(demoDocumentServerProperties.getSecurity().getPrefix())
                            .build()
                    )
                    .build();

            applySettings(documentServerClientSettings);
            return;
        }

        String baseUrl = settings.getUrl();
        if (settings.getInnerUrl() != null && !settings.getInnerUrl().isEmpty()) {
            baseUrl = settings.getInnerUrl();
        }

        Boolean ignoreSSLCertificate = null;
        if (settings.getIgnoreSSLCertificate() != null && !settings.getIgnoreSSLCertificate().isEmpty()) {
            ignoreSSLCertificate = Boolean.parseBoolean(settings.getIgnoreSSLCertificate());
        }

        DocumentServerClientSettings documentServerClientSettings = DocumentServerClientSettings.builder()
                .baseUrl(baseUrl)
                .security(settings.getSecurity())
                .ignoreSSLCertificate(ignoreSSLCertificate)
                .build();

        applySettings(documentServerClientSettings);
    }

    @Override
    public void applySettings(final DocumentServerClientSettings documentServerClientSettings) {
        this.documentServerClientSettings = documentServerClientSettings;
    }

    protected String getBaseUrl() {
        if (documentServerClientSettings.getBaseUrl() != null && !documentServerClientSettings.getBaseUrl().isEmpty()) {
            return documentServerClientSettings.getBaseUrl();
        }

        return urlManager.getInnerDocumentServerUrl();
    }

    protected String getSecurityKey() {
        Security security = documentServerClientSettings.getSecurity();

        if (security != null
                && security.getKey() != null
                && !security.getKey().isEmpty()) {
            return security.getKey();
        }

        return settingsManager.getSecurityKey();
    }

    protected String getSecurityHeader() {
        Security security = documentServerClientSettings.getSecurity();

        if (security != null
                && security.getHeader() != null
                && !security.getHeader().isEmpty()) {
            return security.getHeader();
        }

        return settingsManager.getSecurityHeader();
    }

    protected String getSecurityPrefix() {
        Security security = documentServerClientSettings.getSecurity();

        if (security != null
                && security.getPrefix() != null
                && !security.getPrefix().isEmpty()) {
            return security.getPrefix();
        }

        return settingsManager.getSecurityPrefix();
    }

    protected boolean isSecurityEnabled() {
        Security security = documentServerClientSettings.getSecurity();

        if (security != null
                && security.getKey() != null
                && !security.getKey().isEmpty()) {
            return true;
        }

        return settingsManager.isSecurityEnabled();
    }

    protected HttpClientProperties getHttpClientProperties() {
        HttpClientProperties httpClientProperties = new HttpClientProperties(
                ConfigurationUtils.getDocsIntegrationSdkProperties().getHttpClient()
        );

        Boolean ignoreSSLCertificate = documentServerClientSettings.getIgnoreSSLCertificate();

        if (ignoreSSLCertificate != null) {
            httpClientProperties.setIgnoreSslCertificate(ignoreSSLCertificate);

            return httpClientProperties;
        }

        ignoreSSLCertificate = settingsManager.isIgnoreSSLCertificate();
        httpClientProperties.setIgnoreSslCertificate(ignoreSSLCertificate);

        return httpClientProperties;
    }
}
