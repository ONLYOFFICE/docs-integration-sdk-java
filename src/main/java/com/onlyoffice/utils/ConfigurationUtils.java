/**
 *
 * (c) Copyright Ascensio System SIA 2024
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

package com.onlyoffice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.onlyoffice.model.properties.DocsIntegrationSdkProperties;
import com.onlyoffice.model.properties.docsintegrationsdk.DocumentServerProperties;
import com.onlyoffice.model.properties.docsintegrationsdk.documentserver.SecurityProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigurationUtils {
    /** The properties prefix. */
    private static final String PROPERTIES_PREFIX = "docs-integration-sdk";

    /** The demo security url. */
    private static final String DEMO_URL = "https://onlinedocs.onlyoffice.com/";
    /** The demo security header. */
    private static final String DEMO_SECURITY_HEADER = "AuthorizationJWT";
    /** The demo security key. */
    private static final String DEMO_SECURITY_KEY = "sn2puSUF7muF5Jas";
    /** The demo security prefix. */
    private static final String DEMO_SECURITY_PREFIX = "Bearer ";
    /** The number of days that the demo server can be used. */
    private static final Integer DEMO_TRIAL_PERIOD = 30;

    /** Flag indicating whether settings have been initialized. */
    private static boolean isInit = false;
    /** {@link DocsIntegrationSdkProperties}. */
    private static DocsIntegrationSdkProperties docsIntegrationSdkProperties;

    private ConfigurationUtils() { }

    /**
     * Returns Docs Integration Sdk Properties.
     *
     * @return Docs Integration Sdk Properties.
     */
    public static DocsIntegrationSdkProperties getDocsIntegrationSdkProperties() {
        if (!isInit) {
            init();
            isInit = true;
        }

        return docsIntegrationSdkProperties;
    }

    /**
     * Returns connection settings to the demo server.
     *
     * @return connection settings to the demo server.
     */
    public static DocumentServerProperties getDemoDocumentServerProperties() {
        SecurityProperties securityProperties = new SecurityProperties();
        securityProperties.setHeader(DEMO_SECURITY_HEADER);
        securityProperties.setKey(DEMO_SECURITY_KEY);
        securityProperties.setPrefix(DEMO_SECURITY_PREFIX);

        DocumentServerProperties documentServerProperties = new DocumentServerProperties();
        documentServerProperties.setUrl(DEMO_URL);
        documentServerProperties.setSecurity(securityProperties);
        return documentServerProperties;
    }

    /**
     * Returns the number of days that the demo server can be used.
     *
     * @return the number of days that the demo server can be used.
     */
    public static Integer getDemoTrialPeriod() {
        return DEMO_TRIAL_PERIOD;
    }

    private static void init() {
        Properties properties = new Properties();
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(PROPERTIES_PREFIX + ".properties");
            properties.load(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JavaPropsSchema javaPropsSchema = JavaPropsSchema.emptySchema().withPrefix(PROPERTIES_PREFIX);
        JavaPropsMapper javaPropsMapper = JavaPropsMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
                .build();

        try {
            docsIntegrationSdkProperties = javaPropsMapper.readPropertiesAs(
                    properties,
                    javaPropsSchema,
                    DocsIntegrationSdkProperties.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
