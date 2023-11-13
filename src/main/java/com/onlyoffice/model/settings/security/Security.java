package com.onlyoffice.model.settings.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines the security parameters.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Security {
    /**
     * Defines the secret authorization key.
     */
    private String key;

    /**
     * Defines the authorization header.
     */
    private String header;

    /**
     * Defines the authorization prefix.
     */
    private String prefix;

    /**
     * Specifies if the SSL certificate will be ignored or not.
     */
    private Boolean ignoreSSLCertificate;
}
