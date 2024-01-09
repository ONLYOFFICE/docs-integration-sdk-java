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

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.editorconfig.Customization;
import com.onlyoffice.model.settings.security.Security;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Defines the Document Server settings.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldNameConstants
public class Settings {

    /**
     * Defines the URL to the Document Server.
     */
    private String url;

    /**
     * Defines the internal URL to the Document Server.
     */
    private String innerUrl;

    /**
     * Defines the internal URL to the product.
     */
    private String productInnerUrl;

    /**
     * Defines the security parameters.
     */
    private Security security;

    /**
     * Specifies whether the demo editor is enabled.
     */
    private Boolean demo;

    /**
     * Defines the parameters to customize the editor interface
     * so that it looked like your other products (if there are any)
     * and change the presence or absence of the additional buttons,
     * links, change logos and editor owner details.
     */
    private Customization customization;

    /**
     * Defines a set of extensions that can be edited with the possible loss of information.
     */
    private Set<String> lossyEdit;

    /**
     * Specifies if the SSL certificate will be ignored or not.
     */
    private String ignoreSSLCertificate;

    @JsonAnySetter
    @Builder.Default
    private Map<String, Object> extra = new HashMap<>();
}
