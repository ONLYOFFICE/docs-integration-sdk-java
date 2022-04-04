package com.onlyoffice.springintegrationlib.configuration;

import base.runner.OnlyofficeDefaultConverterRunner;
import core.client.OnlyofficeConverterClient;
import core.model.converter.request.ConverterRequest;
import core.runner.OnlyofficeRunner;
import core.uploader.OnlyofficeUploader;
import exception.OnlyofficeUploaderRuntimeException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class OnlyofficeConverterHandlersSpringConfiguration {
    @ConditionalOnMissingBean(value = ConverterRequest.class, parameterizedContainer = OnlyofficeUploader.class)
    @Bean
    public OnlyofficeUploader<ConverterRequest> emptyConverterUploader() {
        return new OnlyofficeUploader<ConverterRequest>() {
            public void upload(ConverterRequest request, InputStream inputStream) throws OnlyofficeUploaderRuntimeException {

            }
        };
    }

    @ConditionalOnBean(value = ConverterRequest.class, parameterizedContainer = OnlyofficeUploader.class)
    @ConditionalOnMissingBean(value = ConverterRequest.class, parameterizedContainer = OnlyofficeRunner.class)
    @Bean
    public OnlyofficeRunner<ConverterRequest> converterRunner(
            OnlyofficeConverterClient converterClient,
            List<OnlyofficeUploader<ConverterRequest>> converterFileUploaders
    ) {
        return new OnlyofficeDefaultConverterRunner(converterFileUploaders, converterClient);
    }
}
