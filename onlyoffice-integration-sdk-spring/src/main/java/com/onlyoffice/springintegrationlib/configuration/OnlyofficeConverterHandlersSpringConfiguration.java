package com.onlyoffice.springintegrationlib.configuration;

import client.OnlyofficeConverterClient;
import client.OnlyofficeConverterClientRunner;
import client.OnlyofficeConverterClientRunnerBase;
import client.OnlyofficeConverterUploader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OnlyofficeConverterHandlersSpringConfiguration {
    @ConditionalOnBean(OnlyofficeConverterUploader.class)
    @ConditionalOnMissingBean(OnlyofficeConverterClientRunner.class)
    @Bean
    public OnlyofficeConverterClientRunner converterClientRunner(
            OnlyofficeConverterClient converterClient,
            List<OnlyofficeConverterUploader> converterFileUploaders
    ) {
        return new OnlyofficeConverterClientRunnerBase(converterFileUploaders, converterClient);
    }
}
