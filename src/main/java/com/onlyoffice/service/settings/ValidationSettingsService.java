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

package com.onlyoffice.service.settings;

import com.onlyoffice.model.security.Credentials;
import com.onlyoffice.model.settings.validation.ValidationResult;

public interface ValidationSettingsService {
    /**
     * Checks the Document Server.
     *
     * @return The {@link ValidationResult} object.
     * @throws Exception
     */
    ValidationResult checkDocumentServer() throws Exception;

    /**
     * Checks the Document Server by its URL.
     *
     * @param url The URL to the Document Server.
     * @return The {@link ValidationResult} object.
     * @throws Exception
     */
    ValidationResult checkDocumentServer(String url) throws Exception;

    /**
     * Checks the Command Service.
     *
     * @return The {@link ValidationResult} object.
     * @throws Exception
     */
    ValidationResult checkCommandService() throws Exception;

    ValidationResult checkCommandService(String url, Credentials credentials) throws Exception;

    /**
     * Checks the Convert Service.
     *
     * @return The {@link ValidationResult} object.
     * @throws Exception
     */
    ValidationResult checkConvertService() throws Exception;

    ValidationResult checkConvertService(String url, Credentials credentials)
            throws Exception;
}
