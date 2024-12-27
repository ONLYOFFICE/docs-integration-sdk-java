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

package com.onlyoffice.manager.settings;

import com.onlyoffice.model.properties.DocsIntegrationSdkProperties;
import com.onlyoffice.model.settings.Settings;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;


public interface SettingsManager {

    /**
     * Returns a value of the setting with the name specified in the request.
     *
     * @param name The setting name.
     * @return The setting value.
     */
    String getSetting(String name);

    /**
     * Sets a value for the setting with the name specified in the request.
     *
     * @param name The setting name.
     * @param value The setting value.
     */
    void setSetting(String name, String value);

    /**
     * Sets the settings specified with the "Settings" object.
     *
     * @see Settings
     * @param settings The settings to be set.
     * @throws IntrospectionException If the processing fails unexpectedly.
     * @throws InvocationTargetException – If the underlying method throws an exception.
     * @throws IllegalAccessException – If this Method object is enforcing Java language access control and the
     * underlying method is inaccessible.
     */
    void setSettings(Settings settings)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException;

    /**
     * Returns a map of the settings.
     *
     * @return A map of the settings.
     * @throws IntrospectionException If the processing fails unexpectedly.
     * @throws InvocationTargetException – If the underlying method throws an exception.
     * @throws IllegalAccessException – If this Method object is enforcing Java language access control and the
     * underlying method is inaccessible.
     */
    Map<String, String> getSettings()
            throws IntrospectionException, InvocationTargetException, IllegalAccessException;

    /**
     * Returns a boolean value of the setting with the name specified in the request.
     *
     * @param name The setting name.
     * @param defaultValue The setting default value.
     * @return The setting boolean value.
     */
    Boolean getSettingBoolean(String name, Boolean defaultValue);

    /**
     * Returns Docs Integration SDK Properties.
     *
     * @return The object {@link DocsIntegrationSdkProperties}.
     */
    DocsIntegrationSdkProperties getDocsIntegrationSdkProperties();

    /**
     * Checks whether JWT validation is enabled.
     *
     * @return True if JWT validation is enabled.
     */
    Boolean isSecurityEnabled();

    /**
     * Checks whether the setting to ignore SSL certificate is enabled.
     *
     * @return True if the setting to ignore SSL certificate is enabled.
     */
    Boolean isIgnoreSSLCertificate();

    /**
     * Returns the security key.
     *
     * @return The security key.
     */
    String getSecurityKey();

    /**
     * Returns the authorization header.
     *
     * @return The authorization header.
     */
    String getSecurityHeader();

    /**
     * Returns the authorization prefix.
     *
     * @return The authorization prefix.
     */
    String getSecurityPrefix();

    /**
     * Checks whether the demo editor is enabled.
     *
     * @return True if the demo editor is enabled.
     */
    Boolean enableDemo();

    /**
     * Disables the use of the demo editor.
     */
    void disableDemo();

    /**
     * Checks whether the demo editor is active.
     *
     * @return True if the demo editor is enabled and the demo version has not expired.
     */
    Boolean isDemoActive();

    /**
     * Checks whether the demo editor is available.
     *
     * @return True if the demo version has not expired.
     */
    Boolean isDemoAvailable();
}
