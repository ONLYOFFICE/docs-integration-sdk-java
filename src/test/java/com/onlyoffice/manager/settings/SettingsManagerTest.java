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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

@ExtendWith(MockitoExtension.class)
public class SettingsManagerTest {
    private static Properties properties;

    @BeforeEach
    public void setup() {
        properties = new Properties();
    }

    @InjectMocks
    private SettingsManager settingsManager = new DefaultSettingsManager() {
        @Override
        public String getSetting(final String name) {
            return properties.getProperty(name);
        }

        @Override
        public void setSetting(final String name, final String value) {
            properties.put(name, value);
        }
    };

    @Test
    void getSDKSettingsTest() {
        System.out.println(settingsManager.getSDKSetting("integration-sdk.api.url"));
    }

    @Test
    void isSecurityEnabledTest() {
        Assertions.assertEquals(false, settingsManager.isSecurityEnabled());

        settingsManager.setSetting(SettingsConstants.SECURITY_KEY, "secret");
        Assertions.assertEquals(true, settingsManager.isSecurityEnabled());

        settingsManager.setSetting(SettingsConstants.SECURITY_KEY, "");
        Assertions.assertEquals(false, settingsManager.isSecurityEnabled());

        settingsManager.enableDemo();
        Assertions.assertEquals(true, settingsManager.isSecurityEnabled());

        settingsManager.disableDemo();
        Assertions.assertEquals(false, settingsManager.isSecurityEnabled());
    }

    @Test
    void getSecuritySecretTest() {
        Assertions.assertEquals(null, settingsManager.getSecurityKey());

        settingsManager.setSetting(SettingsConstants.SECURITY_KEY, "secret");
        Assertions.assertEquals("secret", settingsManager.getSecurityKey());
    }

    @Test
    void getSecurityHeaderTest() {
        Assertions.assertEquals("Authorization", settingsManager.getSecurityHeader());

        settingsManager.setSetting(SettingsConstants.SECURITY_HEADER, "AuthorizationJwt");
        Assertions.assertEquals("AuthorizationJwt", settingsManager.getSecurityHeader());
    }

    @Test
    void getSecurityPrefixTest() {
        Assertions.assertEquals("Bearer ", settingsManager.getSecurityPrefix());

        settingsManager.setSetting(SettingsConstants.SECURITY_PREFIX, "Bearer2 ");
        Assertions.assertEquals("Bearer2 ", settingsManager.getSecurityPrefix());
    }

    @Test
    void isIgnoreSSLCertificateTest() {
        Assertions.assertEquals(false, settingsManager.isIgnoreSSLCertificate());

        settingsManager.setSetting(SettingsConstants.SECURITY_IGNORE_SSL_CERTIFICATE, "true");
        Assertions.assertEquals(true, settingsManager.isIgnoreSSLCertificate());

        settingsManager.enableDemo();
        Assertions.assertEquals(false, settingsManager.isIgnoreSSLCertificate());

        settingsManager.disableDemo();
        Assertions.assertEquals(true, settingsManager.isIgnoreSSLCertificate());
    }

    @Test
    void enableDemoTest() throws ParseException {
        Assertions.assertEquals(true, settingsManager.enableDemo());

        Assertions.assertEquals(
                true,
                settingsManager.getSettingBoolean(SettingsConstants.DEMO, false)
        );

        String demoStart = settingsManager.getSetting("demo-start");

        Assertions.assertEquals(true, demoStart != null && !demoStart.isEmpty());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar dateDemoStart = Calendar.getInstance();
        dateDemoStart.setTime(dateFormat.parse(demoStart));
        dateDemoStart.add(
                Calendar.DATE,
                -Integer.parseInt(settingsManager.getSDKSetting("integration-sdk.demo.trial-period"))
        );

        settingsManager.setSetting(SettingsConstants.DEMO, "false");
        settingsManager.setSetting(
                "demo-start",
                dateFormat.format(new Date(dateDemoStart.getTimeInMillis()))
        );

        Assertions.assertEquals(false, settingsManager.enableDemo());
    }

    @Test
    void disableDemoTest() {
        settingsManager.disableDemo();

        String demo = settingsManager.getSetting(SettingsConstants.DEMO);

        Assertions.assertEquals(true, demo != null && !demo.isEmpty());
        Assertions.assertEquals(false, Boolean.parseBoolean(demo));
    }

    @Test
    void isDemoActive() throws ParseException {
        settingsManager.enableDemo();
        Assertions.assertEquals(true, settingsManager.isDemoActive());

        settingsManager.disableDemo();
        Assertions.assertEquals(false, settingsManager.isDemoActive());

        settingsManager.enableDemo();
        String demoStart = settingsManager.getSetting("demo-start");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar dateDemoStart = Calendar.getInstance();
        dateDemoStart.setTime(dateFormat.parse(demoStart));
        dateDemoStart.add(
                Calendar.DATE,
                -Integer.parseInt(settingsManager.getSDKSetting("integration-sdk.demo.trial-period"))
        );
        settingsManager.setSetting(
                "demo-start",
                dateFormat.format(new Date(dateDemoStart.getTimeInMillis()))
        );

        Assertions.assertEquals(false, settingsManager.isDemoActive());
    }

    @Test
    void isDemoAvailableTest() {
        Assertions.assertEquals(true, settingsManager.isDemoAvailable());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        settingsManager.setSetting("demo-start", dateFormat.format(date));
        Assertions.assertEquals(true, settingsManager.isDemoAvailable());

        Calendar dateDemoStart = Calendar.getInstance();
        dateDemoStart.setTime(date);
        dateDemoStart.add(
                Calendar.DATE,
                -Integer.parseInt(settingsManager.getSDKSetting("integration-sdk.demo.trial-period"))
        );
        settingsManager.setSetting(
                "demo-start",
                dateFormat.format(new Date(dateDemoStart.getTimeInMillis()))
        );
        Assertions.assertEquals(false, settingsManager.isDemoAvailable());
    }
}
