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

import com.onlyoffice.model.documenteditor.config.editorconfig.Customization;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Anonymous;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Customer;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Features;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Goback;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Logo;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Review;
import com.onlyoffice.model.settings.Settings;
import com.onlyoffice.model.settings.SettingsConstants;
import com.onlyoffice.model.settings.security.Security;

import java.beans.BeanInfo;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class DefaultSettingsManager implements SettingsManager {
    private static Properties properties;

    static {
        init();
    }

    public abstract String getSetting(String name);

    public abstract Void setSetting(String name, String value);

    public void setSettings(final Settings settings)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
         Map<String, String> mapSettings = convertObjectToDotNotationMap(settings);

         for (Map.Entry<String, String> setting : mapSettings.entrySet()) {
             setSetting(setting.getKey(), setting.getValue());
         }
    }

    public Map<String, String> getSettings()
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Settings settings = Settings.builder()
                .security(
                        Security.builder().build()
                )
                .customization(
                        Customization.builder()
                                .goback(
                                        Goback.builder().build()
                                )
                                .anonymous(
                                        Anonymous.builder().build()
                                )
                                .customer(
                                        Customer.builder().build()
                                )
                                .features(
                                        Features.builder().build()
                                )
                                .logo(
                                        Logo.builder().build()
                                )
                                .review(
                                        Review.builder().build()
                                )
                                .build()
                )
                .build();
        List<String> namesSettings = getNamesSettings(settings);

        Map<String, String> settinsMap = new HashMap<>();

        for (String name : namesSettings) {
            settinsMap.put(name, getSetting(name));
        }

        return settinsMap;
    }

    @Override
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
        String key = getSecurityKey();
        return key != null && !key.isEmpty();
    }

    public String getSecurityKey() {
        if (isDemoActive()) {
            return getSDKSetting("integration-sdk.demo.security.key");
        } else {
            String key = getSetting(SettingsConstants.SECURITY_KEY);
            if (key == null || key.isEmpty()) {
                key = getSDKSetting("integration-sdk.security.key");
            }

            if (key != null && !key.isEmpty()) {
                return key;
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
        String demoStart = getSetting("demo-start");
        if (demoStart == null || demoStart.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            setSetting("demo-start", dateFormat.format(date));
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

        String demoStart = getSetting("demo-start");
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
        String demoStart = getSetting("demo-start");
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

    private <T> Map<String, String> convertObjectToDotNotationMap(final T object)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<T> beanClass = (Class<T>) object.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);

        Map<String, String> result = new HashMap<>();

        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            String name = propertyDescriptor.getName();
            Object value = propertyDescriptor.getReadMethod().invoke(object);
            if (value != null && !name.equals("class")) {
                if (value.toString().startsWith("com.onlyoffice.model")) {
                    for (Map.Entry<String, String> map : convertObjectToDotNotationMap(value).entrySet()) {
                        result.put(name + "." + map.getKey(), map.getValue());
                    }
                } else {
                    result.put(name, value.toString());
                }
            }
        }

        return result;
    }

    private <T> List<String> getNamesSettings(final T object)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<T> beanClass = (Class<T>) object.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);

        List<String> result = new ArrayList<>();

        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            String name = propertyDescriptor.getName();
            Object value = propertyDescriptor.getReadMethod().invoke(object);
            if (!name.equals("class")) {
                if (value != null && value.toString().startsWith("com.onlyoffice.model")) {
                    for (String name1 : getNamesSettings(value)) {
                        result.add(name + "." + name1);
                    }
                } else {
                    result.add(name);
                }
            }
        }

        return result;
    }
}
