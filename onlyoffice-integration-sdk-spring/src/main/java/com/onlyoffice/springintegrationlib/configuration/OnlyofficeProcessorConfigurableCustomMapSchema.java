package com.onlyoffice.springintegrationlib.configuration;

import core.processor.schema.OnlyofficeProcessorCustomMapSchema;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnlyofficeProcessorConfigurableCustomMapSchema implements OnlyofficeProcessorCustomMapSchema {
    private final String secretKey;
    private final String tokenKey;

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getTokenKey() {
        return this.tokenKey;
    }
}
