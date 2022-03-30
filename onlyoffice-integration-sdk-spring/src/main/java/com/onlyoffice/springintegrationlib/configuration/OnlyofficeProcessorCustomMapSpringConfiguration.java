package com.onlyoffice.springintegrationlib.configuration;

import core.processor.configuration.OnlyofficeDefaultPrePostProcessorCustomMapConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeProcessorCustomMapSpringConfiguration {
    @Bean
    public OnlyofficeDefaultPrePostProcessorCustomMapConfiguration configuration() {
        return new OnlyofficeDefaultPrePostProcessorCustomMapConfiguration();
    }
}
