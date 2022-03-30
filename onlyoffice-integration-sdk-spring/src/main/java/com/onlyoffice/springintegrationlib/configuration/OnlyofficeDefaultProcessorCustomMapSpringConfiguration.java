package com.onlyoffice.springintegrationlib.configuration;

import core.processor.configuration.OnlyofficeDefaultProcessorCustomMapConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeDefaultProcessorCustomMapSpringConfiguration {
    @Bean
    public OnlyofficeDefaultProcessorCustomMapConfiguration configuration() {
        return new OnlyofficeDefaultProcessorCustomMapConfiguration();
    }
}
