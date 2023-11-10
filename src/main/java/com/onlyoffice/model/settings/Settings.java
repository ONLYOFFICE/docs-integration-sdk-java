package com.onlyoffice.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.editorconfig.Customization;
import com.onlyoffice.model.settings.security.Security;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Defines the setting to edit the specified extensions with the possible loss of information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
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
}
