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

package com.onlyoffice.manager.url;

import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.service.convert.DefaultConvertService;
import com.onlyoffice.utils.ConfigurationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlManagerTest {
    @Mock
    private SettingsManager settingsManager;
    @Mock
    private DocumentManager documentManager;
    @Mock
    private RequestManager requestManager;

    private UrlManager urlManager;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        urlManager = new DefaultUrlManager(settingsManager);
    }

    @Test
    void getFileExtensionTest() {
        RequestedService requestedService = new DefaultConvertService(
                documentManager,
                urlManager,
                requestManager,
                settingsManager
        );

        when(settingsManager.getDocsIntegrationSdkProperties()).thenReturn(
                ConfigurationUtils.getDocsIntegrationSdkProperties()
        );

        Assertions.assertEquals(true, urlManager.getServiceUrl(requestedService).endsWith(
                ConfigurationUtils.getDocsIntegrationSdkProperties().getDocumentServer().getConvertService().getUrl()
        ));
    }
}
