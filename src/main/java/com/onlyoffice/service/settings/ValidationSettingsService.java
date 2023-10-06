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

import com.onlyoffice.model.settings.validation.ValidationResult;

public interface ValidationSettingsService {
    /**
     * Check Document Server.
     *
     * @return the object {@link ValidationResult}
     * @throws Exception
     */
    ValidationResult checkDocumentServer() throws Exception;

    /**
     * Check Document Server.
     *
     * @param url the url to Document Server.
     * @return the object {@link ValidationResult}
     * @throws Exception
     */
    ValidationResult checkDocumentServer(String url) throws Exception;

    /**
     * Check Command Service.
     *
     * @return the object {@link ValidationResult}
     * @throws Exception
     */
    ValidationResult checkCommandService() throws Exception;

    /**
     * Check Command Service.
     *
     * @param url the url to Document Server.
     * @param secretKey the secret authorization key
     * @param jwtHeader the authorization header
     * @param jwtPrefix the authorization prefix
     * @return the object {@link ValidationResult}
     * @throws Exception
     */
    ValidationResult checkCommandService(String url, String secretKey, String jwtHeader, String jwtPrefix)
            throws Exception;

    /**
     * Check Convert Service.
     *
     * @return the object {@link ValidationResult}
     * @throws Exception
     */
    ValidationResult checkConvertService() throws Exception;

    /**
     * Check Convert Service.
     *
     * @param url the url to Document Server.
     * @param secretKey the secret authorization key
     * @param jwtHeader the authorization header
     * @param jwtPrefix the authorization prefix
     * @return the object {@link ValidationResult}
     * @throws Exception
     */
    ValidationResult checkConvertService(String url, String secretKey, String jwtHeader, String jwtPrefix)
            throws Exception;
}
