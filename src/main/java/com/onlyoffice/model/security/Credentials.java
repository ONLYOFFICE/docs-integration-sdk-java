package com.onlyoffice.model.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the credentials.
 */
@Getter
@Setter
@Builder
public class Credentials {
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
}
