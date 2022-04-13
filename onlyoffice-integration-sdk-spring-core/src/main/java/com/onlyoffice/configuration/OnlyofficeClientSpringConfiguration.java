package com.onlyoffice.configuration;

import base.client.OnlyofficeDefaultCommandClient;
import base.client.OnlyofficeDefaultConverterClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.client.OnlyofficeCommandClient;
import core.client.OnlyofficeConverterClient;
import core.security.OnlyofficeJwtSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeClientSpringConfiguration {
    @Value("${onlyoffice.client.commandConnectTimeout:0}")
    private int commandConnectTimeout;
    @Value("${onlyoffice.client.commandResponseTimeout:0}")
    private int commandResponseTimeout;
    @Value("${onlyoffice.client.converterConnectTimeout:0}")
    private int converterConnectTimeout;
    @Value("${onlyoffice.client.converterResponseTimeout:0}")
    private int converterResponseTimeout;

    @Bean
    public OnlyofficeCommandClient onlyofficeCommandClient(
            OnlyofficeJwtSecurity jwtSecurity,
            ObjectMapper objectMapper
    ) {
        OnlyofficeDefaultCommandClient commandClient = new OnlyofficeDefaultCommandClient(jwtSecurity, objectMapper);
        if (this.commandConnectTimeout > 0) commandClient.setConnectTimeout(this.commandConnectTimeout);
        if (this.commandResponseTimeout > 0) commandClient.setResponseTimeout(this.commandResponseTimeout);
        return commandClient;
    }

    @Bean
    public OnlyofficeConverterClient onlyofficeConverterClient(
            OnlyofficeJwtSecurity jwtSecurity,
            ObjectMapper objectMapper
    ) {
        OnlyofficeDefaultConverterClient converterClient = new OnlyofficeDefaultConverterClient(jwtSecurity, objectMapper);
        if (this.converterConnectTimeout > 0) converterClient.setConnectTimeout(this.converterConnectTimeout);
        if (this.converterResponseTimeout > 0) converterClient.setResponseTimeout(this.converterResponseTimeout);
        return converterClient;
    }
}
