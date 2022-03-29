package com.onlyoffice.springintegrationlib.configuration;

import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeSecuritySpringConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeJwtManager onlyofficeJwtManager() {
        return new OnlyofficeJwtManagerBase();
    }
}
