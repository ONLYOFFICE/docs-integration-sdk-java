package com.onlyoffice.configuration;

import base.uploader.OnlyofficeDefaultConverterUploaderRunner;
import core.client.OnlyofficeConverterClient;
import core.model.converter.request.ConverterRequest;
import core.uploader.OnlyofficeUploader;
import core.uploader.OnlyofficeUploaderRunner;
import exception.OnlyofficeUploaderRuntimeException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class OnlyofficeConverterHandlersSpringConfiguration {
    @Bean
    public OnlyofficeUploader<ConverterRequest> emptyConverterUploader() {
        return new OnlyofficeUploader<ConverterRequest>() {
            public void upload(ConverterRequest request, InputStream inputStream) throws OnlyofficeUploaderRuntimeException {}
        };
    }

    @Bean
    public OnlyofficeUploaderRunner<ConverterRequest> converterRunner(
            OnlyofficeConverterClient converterClient,
            List<OnlyofficeUploader<ConverterRequest>> converterFileUploaders
    ) {
        return new OnlyofficeDefaultConverterUploaderRunner(converterFileUploaders, converterClient);
    }
}
