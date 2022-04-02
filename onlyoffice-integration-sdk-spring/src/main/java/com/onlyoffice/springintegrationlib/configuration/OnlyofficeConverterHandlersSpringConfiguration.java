package com.onlyoffice.springintegrationlib.configuration;

import client.OnlyofficeConverterClient;
import client.OnlyofficeConverterClientRunner;
import client.OnlyofficeConverterClientRunnerBase;
import client.OnlyofficeConverterUploader;
import core.model.converter.request.ConverterRequest;
import core.uploader.OnlyofficeFileUploaderType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class OnlyofficeConverterHandlersSpringConfiguration {
    @ConditionalOnMissingBean(OnlyofficeConverterUploader.class)
    @Bean
    public OnlyofficeConverterUploader emptyConverterUploader() {
        return new OnlyofficeConverterUploader() {
            public void upload(ConverterRequest request, InputStream inputStream) {}
            public OnlyofficeFileUploaderType getType() {
                return OnlyofficeFileUploaderType.FILE;
            }
        };
    }

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
