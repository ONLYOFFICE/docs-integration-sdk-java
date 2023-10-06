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

package com.onlyoffice.manager.settings;

public interface SettingsManager {

    /**
     * Gets setting value.
     *
     * @param name the setting name
     * @return the setting value
     */
    String getSetting(String name);

    /**
     * Sets setting value.
     *
     * @param name the setting name
     * @param value the setting value
     */
    void setSetting(String name, String value);

    /**
     * Gets setting boolean value.
     *
     * @param name the setting name
     * @param defaultValue the setting default value
     * @return the setting boolean value
     */
    Boolean getSettingBoolean(String name, Boolean defaultValue);

    /**
     * Gets setting from file "settings.properties".
     *
     * @param name the setting name
     * @return the setting value
     */
    String getSDKSetting(String name);

    /**
     * Check if jwt check is enabled.
     *
     * @return true if jwt check is enabled
     */
    Boolean isSecurityEnabled();

    /**
     * Check if the setting to ignore ssl certificate is enabled.
     *
     * @return true if setting to ignore ssl certificate is enabled
     */
    Boolean isIgnoreSSLCertificate();

    /**
     * Gets security secret key.
     *
     * @return the security secret key
     */
    String getSecuritySecret();

    /**
     * Gets authorization header.
     *
     * @return the authorization header
     */
    String getSecurityHeader();

    /**
     * Gets authorization prefix.
     *
     * @return the authorization prefix
     */
    String getSecurityPrefix();

    /**
     * Сheck if the demo editor is enabled.
     *
     * @return true if settings demo is enabled
     */
    Boolean enableDemo();

    /**
     * Disable use of demo editor.
     */
    void disableDemo();

    /**
     * Сheck if the demo editor is active.
     *
     * @return true if settings demo is enabled and period using demo not is over
     */
    Boolean isDemoActive();

    /**
     * Сheck if the demo editor is available.
     *
     * @return true if period using demo not is over
     */
    Boolean isDemoAvailable();
}
