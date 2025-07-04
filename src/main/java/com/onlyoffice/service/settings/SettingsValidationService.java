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

package com.onlyoffice.service.settings;

import com.onlyoffice.model.settings.HttpClientSettings;
import com.onlyoffice.model.settings.security.Security;
import com.onlyoffice.model.settings.validation.ValidationResult;


public interface SettingsValidationService {

    /**
     * Checks the Document Server.
     *
     * @return The {@link ValidationResult} object.
     * @throws Exception If the processing fails unexpectedly.
     */
    ValidationResult checkDocumentServer() throws Exception;

    /**
     * Checks the Document Server by its URL.
     *
     * @param url The URL to the Document Server.
     * @param httpClientSettings The settings for http client.
     * @see HttpClientSettings
     * @return The {@link ValidationResult} object.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    ValidationResult checkDocumentServer(String url, HttpClientSettings httpClientSettings) throws Exception;

    /**
     * Checks the Command Service.
     *
     * @return The {@link ValidationResult} object.
     * @throws Exception If the processing fails unexpectedly.
     */
    ValidationResult checkCommandService() throws Exception;

    /**
     * Checks the Command Service by the Document Server URL.
     *
     * @param url The URL to the Document Server.
     * @param security The security parameters.
     * @param httpClientSettings The settings for http client.
     * @see Security
     * @return The {@link ValidationResult} object.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    ValidationResult checkCommandService(String url, Security security, HttpClientSettings httpClientSettings)
            throws Exception;

    /**
     * Checks the Convert Service.
     *
     * @return The {@link ValidationResult} object.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    ValidationResult checkConvertService() throws Exception;

    /**
     * Checks the Convert Service by the Document Server URL.
     *
     * @param productUrl The URL to the integration product.
     * @return The {@link ValidationResult} object.
     */
    ValidationResult checkConvertService(String productUrl);

    /**
     * Checks the Convert Service by the Document Server URL.
     *
     * @param url The URL to the Document Server.
     * @param productInnerUrl The internal URL to the integration product.
     * @param security The security parameters.
     * @param httpClientSettings The settings for http client.
     * @see Security
     * @see HttpClientSettings
     * @return The {@link ValidationResult} object.
     * @throws Exception If the processing fails unexpectedly.
     */
    @Deprecated
    ValidationResult checkConvertService(String url, String productInnerUrl, Security security,
                                         HttpClientSettings httpClientSettings) throws Exception;
}
