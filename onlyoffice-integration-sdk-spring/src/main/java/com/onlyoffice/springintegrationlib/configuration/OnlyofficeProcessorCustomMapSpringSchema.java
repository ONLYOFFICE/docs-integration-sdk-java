package com.onlyoffice.springintegrationlib.configuration;

import core.processor.schema.OnlyofficeDefaultPrePostProcessorMapSchema;
import core.processor.schema.OnlyofficeProcessorCustomMapSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeProcessorCustomMapSpringSchema {
    @Value("${onlyoffice.processor.custom.secretKey:secret}")
    private String customSecretKey;
    @Value("${onlyoffice.processor.custom.tokenKey:token}")
    private String customTokenKey;
    @Bean
    public OnlyofficeDefaultPrePostProcessorMapSchema defaultMapSchema() {
        return new OnlyofficeDefaultPrePostProcessorMapSchema();
    }
    @Bean
    public OnlyofficeProcessorCustomMapSchema customMapSchema() {
        return new OnlyofficeProcessorConfigurableCustomMapSchema(this.customSecretKey, this.customTokenKey);
    }
}
