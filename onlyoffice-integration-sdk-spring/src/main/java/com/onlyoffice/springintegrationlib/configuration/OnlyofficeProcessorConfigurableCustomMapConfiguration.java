package com.onlyoffice.springintegrationlib.configuration;

import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnlyofficeProcessorConfigurableCustomMapConfiguration implements OnlyofficeProcessorCustomMapConfiguration {
    private final String secretKey;
    private final String tokenKey;

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getTokenKey() {
        return this.tokenKey;
    }
}
