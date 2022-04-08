package com.onlyoffice.springintegrationlib.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeSecuritySpringConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeJwtSecurity onlyofficeJwtManager(
            ObjectMapper objectMapper
    ) {
        return new OnlyofficeJwtSecurityManager(objectMapper);
    }
}
