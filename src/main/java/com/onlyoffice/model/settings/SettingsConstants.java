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

package com.onlyoffice.model.settings;


/**
 * Defines the settings constants.
 */
public final class SettingsConstants {
    private SettingsConstants() { }

    /**
     * Defines the URL to the Document Server.
     */
    public static final String URL = "url";

    /**
     * Defines the internal URL to the Document Server.
     */
    public static final String INNER_URL = "innerUrl";

    /**
     * Defines the internal URL to the product.
     */
    public static final String PRODUCT_INNER_URL = "productInnerUrl";

    /**
     * Defines the secret authorization key.
     */
    public static final String SECURITY_KEY = "security.key";

    /**
     * Defines the authorization header.
     */
    public static final String SECURITY_HEADER = "security.header";

    /**
     * Defines the authorization prefix.
     */
    public static final String SECURITY_PREFIX  = "security.prefix";

    /**
     * Defines the setting to ignore SSL certificate.
     */
    public static final String SECURITY_IGNORE_SSL_CERTIFICATE = "security.ignoreSSLCertificate";

    /**
     * Defines the demo editor.
     */
    public static final String DEMO = "demo";

    /**
     * Defines the setting to edit the specified extensions with the possible loss of information.
     */
    public static final String LOSSY_EDIT = "lossyEdit";
}
