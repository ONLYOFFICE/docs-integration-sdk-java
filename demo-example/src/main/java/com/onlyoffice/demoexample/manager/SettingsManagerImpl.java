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

package com.onlyoffice.demoexample.manager;

import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.model.settings.SettingsConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SettingsManagerImpl extends DefaultSettingsManager {
    private static Properties properties;

    static {
        properties = new Properties();
    }

    public SettingsManagerImpl(
            @Value("${url}") final String docServerUrl,
            @Value("${security.key}") final String securityKey
    ) {
        properties.put(SettingsConstants.URL, docServerUrl);
        properties.put(SettingsConstants.SECURITY_KEY, securityKey);
    }

    @Override
    public String getSetting(final String name) {
        return properties.getProperty(name);
    }

    @Override
    public void setSetting(final String name, final String value) {
        properties.setProperty(name, value);
    }
}
