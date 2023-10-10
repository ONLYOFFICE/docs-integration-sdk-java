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


import com.onlyoffice.model.settings.SettingsConstants;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public abstract class DefaultSettingsManager implements SettingsManager {
    private static Properties properties;

    static {
        init();
    }

    public abstract String getSetting(String name);

    public abstract Void setSetting(String name, String value);

    public Boolean getSettingBoolean(final String name, final Boolean defaultValue) {
        String setting = getSetting(name);

        if (setting == null || setting.isEmpty()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(setting);
    }

    public String getSDKSetting(final String name) {
        if (properties == null) {
            return null;
        }

       return properties.getProperty(name);
    }

    public Boolean isSecurityEnabled() {
        String secret = getSecuritySecret();
        return secret != null && !secret.isEmpty();
    }

    public String getSecuritySecret() {
        if (isDemoActive()) {
            return getSDKSetting("integration-sdk.demo.security.secret");
        } else {
            String secret = getSetting(SettingsConstants.SECURITY_SECRET);
            if (secret == null || secret.isEmpty()) {
                secret = getSDKSetting("integration-sdk.security.secret");
            }

            if (secret != null && !secret.isEmpty()) {
                return secret;
            }

            return null;
        }
    }

    public String getSecurityHeader() {
        if (isDemoActive()) {
            return getSDKSetting("integration-sdk.demo.security.header");
        } else {
            String header = getSetting(SettingsConstants.SECURITY_HEADER);
            if (header == null || header.isEmpty()) {
                header = getSDKSetting("integration-sdk.security.header");
            }

            if (header != null && !header.isEmpty()) {
                return header;
            }

            return null;
        }
    }

    public String getSecurityPrefix() {
        if (isDemoActive()) {
            return getSDKSetting("integration-sdk.demo.security.prefix");
        } else {
            String prefix = getSetting(SettingsConstants.SECURITY_PREFIX);
            if (prefix == null || prefix.isEmpty()) {
                prefix = getSDKSetting("integration-sdk.security.prefix");
            }

            if (prefix != null && !prefix.isEmpty()) {
                return prefix;
            }

            return "";
        }
    }

    public Boolean isIgnoreSSLCertificate() {
        if (!isDemoActive()) {
            String ignoreSSLCertificate = getSetting(SettingsConstants.SECURITY_IGNORE_SSL_CERTIFICATE);
            if (ignoreSSLCertificate == null || ignoreSSLCertificate.isEmpty()) {
                ignoreSSLCertificate =  getSDKSetting("integration-sdk.security.ignore-ssl-certificate");
            }

            if (ignoreSSLCertificate != null && !ignoreSSLCertificate.isEmpty()) {
                return Boolean.parseBoolean(ignoreSSLCertificate);
            }
        }

        return false;
    }

    public Boolean enableDemo() {
        setSetting(SettingsConstants.DEMO, "true");
        String demoStart = getSetting(SettingsConstants.DEMO_START);
        if (demoStart == null || demoStart.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            setSetting(SettingsConstants.DEMO_START, dateFormat.format(date));
            return true;
        } else {
            return false;
        }
    }

    public void disableDemo() {
        setSetting(SettingsConstants.DEMO, "false");
    }

    @Override
    public Boolean isDemoActive() {
        String demo = getSetting(SettingsConstants.DEMO);

        if (demo == null || demo.isEmpty()) {
            return false;
        }

        Boolean isDemo = Boolean.parseBoolean(demo);

        String demoStart = getSetting(SettingsConstants.DEMO_START);
        if (demoStart != null && !demoStart.isEmpty() && isDemo) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                Calendar date = Calendar.getInstance();
                date.setTime(dateFormat.parse(demoStart));
                date.add(Calendar.DATE, Integer.parseInt(getSDKSetting("integration-sdk.demo.trial-period")));

                return date.after(Calendar.getInstance());
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public Boolean isDemoAvailable() {
        String demoStart = getSetting(SettingsConstants.DEMO_START);
        if (demoStart != null && !demoStart.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                Calendar date = Calendar.getInstance();
                date.setTime(dateFormat.parse(demoStart));
                date.add(Calendar.DATE, Integer.parseInt(getSDKSetting("integration-sdk.demo.trial-period")));

                return date.after(Calendar.getInstance());
            } catch (Exception e) {
                return false;
            }
        } else {
            return true;
        }
    }

    private static void init() {
        try {
            properties = new Properties();
            InputStream stream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("settings.properties");
            properties.load(stream);
        } catch (Exception e) {
            properties = null;
        }
    }

}
