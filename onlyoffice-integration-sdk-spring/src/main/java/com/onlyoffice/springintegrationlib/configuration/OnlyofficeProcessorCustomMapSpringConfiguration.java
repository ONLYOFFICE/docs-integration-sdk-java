package com.onlyoffice.springintegrationlib.configuration;

import core.processor.configuration.OnlyofficeDefaultPrePostProcessorCustomMapConfiguration;
import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeProcessorCustomMapSpringConfiguration {
    @Value("${onlyoffice.processor.custom.secretKey:secret}")
    private String customSecretKey;
    @Value("${onlyoffice.processor.custom.tokenKey:token}")
    private String customTokenKey;
    @Bean
    public OnlyofficeDefaultPrePostProcessorCustomMapConfiguration configuration() {
        return new OnlyofficeDefaultPrePostProcessorCustomMapConfiguration();
    }
    @Bean
    public OnlyofficeProcessorCustomMapConfiguration customMapConfiguration() {
        return new OnlyofficeProcessorConfigurableCustomMapConfiguration(this.customSecretKey, this.customTokenKey);
    }
}
