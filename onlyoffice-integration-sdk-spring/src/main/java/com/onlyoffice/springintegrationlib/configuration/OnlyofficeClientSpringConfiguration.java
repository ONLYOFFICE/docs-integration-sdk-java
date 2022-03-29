package com.onlyoffice.springintegrationlib.configuration;

import client.OnlyofficeCommandClient;
import client.OnlyofficeCommandClientBase;
import client.OnlyofficeConverterClient;
import client.OnlyofficeConverterClientBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.security.OnlyofficeJwtManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "onlyoffice.client")
public class OnlyofficeClientSpringConfiguration {
    private int commandConnectTimeout;
    private int commandResponseTimeout;
    private int converterConnectTimeout;
    private int converterResponseTimeout;

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCommandClient onlyofficeCommandClient(
            OnlyofficeJwtManager jwtManager,
            ObjectMapper objectMapper
    ) {
        OnlyofficeCommandClientBase commandClient = new OnlyofficeCommandClientBase(jwtManager, objectMapper);
        if (this.commandConnectTimeout > 0) commandClient.setConnectTimeout(this.commandConnectTimeout);
        if (this.commandResponseTimeout > 0) commandClient.setResponseTimeout(this.commandResponseTimeout);
        return commandClient;
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeConverterClient onlyofficeConverterClient(
            OnlyofficeJwtManager jwtManager,
            ObjectMapper mapper
    ) {
        OnlyofficeConverterClientBase converterClient = new OnlyofficeConverterClientBase(jwtManager, mapper);
        if (this.converterConnectTimeout > 0) converterClient.setConnectTimeout(this.converterConnectTimeout);
        if (this.converterResponseTimeout > 0) converterClient.setResponseTimeout(this.converterResponseTimeout);
        return converterClient;
    }
}
