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

package com.onlyoffice.model.properties.docsintegrationsdk;

import com.onlyoffice.model.properties.docsintegrationsdk.documentserver.CommandServiceProperties;
import com.onlyoffice.model.properties.docsintegrationsdk.documentserver.ConvertServiceProperties;
import com.onlyoffice.model.properties.docsintegrationsdk.documentserver.DocbuilderServiceProperties;
import com.onlyoffice.model.properties.docsintegrationsdk.documentserver.EditingServiceProperties;
import com.onlyoffice.model.properties.docsintegrationsdk.documentserver.SecurityProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentServerProperties {
    private String url;
    private String apiUrl;
    private String apiPreloaderUrl;
    private String healthCheckUrl;
    private SecurityProperties security;
    private EditingServiceProperties editingService;
    private ConvertServiceProperties convertService;
    private CommandServiceProperties commandService;
    private DocbuilderServiceProperties docbuilderService;
    private String mobileUserAgent;
}
