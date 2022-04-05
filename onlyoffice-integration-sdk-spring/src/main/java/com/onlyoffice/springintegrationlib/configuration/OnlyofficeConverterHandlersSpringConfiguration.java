package com.onlyoffice.springintegrationlib.configuration;

import base.uploader.OnlyofficeDefaultConverterUploaderRunner;
import core.client.OnlyofficeConverterClient;
import core.model.converter.request.ConverterRequest;
import core.uploader.OnlyofficeUploader;
import core.uploader.OnlyofficeUploaderRunner;
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
    @ConditionalOnMissingBean(value = ConverterRequest.class, parameterizedContainer = OnlyofficeUploaderRunner.class)
    @Bean
    public OnlyofficeUploaderRunner<ConverterRequest> converterRunner(
            OnlyofficeConverterClient converterClient,
            List<OnlyofficeUploader<ConverterRequest>> converterFileUploaders
    ) {
        return new OnlyofficeDefaultConverterUploaderRunner(converterFileUploaders, converterClient);
    }
}
