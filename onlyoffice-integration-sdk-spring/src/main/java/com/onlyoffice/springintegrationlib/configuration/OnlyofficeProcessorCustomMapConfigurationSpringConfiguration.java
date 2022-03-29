package com.onlyoffice.springintegrationlib.configuration;

import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
public class OnlyofficeProcessorCustomMapConfigurationSpringConfiguration implements OnlyofficeProcessorCustomMapConfiguration {
    @Value("${onlyoffice.processor.beforeMapKey:beforeMap}")
    private String beforeMapKey;
    @Value("${onlyoffice.processor.afterMapKey:afterMap}")
    private String afterMapKey;
    @Value("${onlyoffice.processor.secretKey:secret}")
    private String secretKey;
    @Value("${onlyoffice.processor.tokenKey:token}")
    private String tokenKey;
    @Value("${onlyoffice.processor.autoFillerKey:autoFiller}")
    private String autoFillerKey;

    public String getBeforeMapKey() {
        return this.beforeMapKey;
    }

    public String getAfterMapKey() {
        return this.afterMapKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getTokenKey() {
        return this.tokenKey;
    }

    public String getAutoFillerKey() {
        return this.autoFillerKey;
    }
}
