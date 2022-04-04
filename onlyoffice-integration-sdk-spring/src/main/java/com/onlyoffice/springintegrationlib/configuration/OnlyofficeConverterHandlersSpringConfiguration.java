package com.onlyoffice.springintegrationlib.configuration;

import base.runner.OnlyofficeDefaultConverterRunner;
import core.client.OnlyofficeConverterClient;
import core.model.converter.request.ConverterRequest;
import core.runner.OnlyofficeConverterRunner;
import core.uploader.OnlyofficeConverterUploader;
import core.uploader.OnlyofficeUploaderType;
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
            public OnlyofficeUploaderType getUploaderType() { return OnlyofficeUploaderType.FILE; }
        };
    }

    @ConditionalOnBean(OnlyofficeConverterUploader.class)
    @ConditionalOnMissingBean(OnlyofficeConverterRunner.class)
    @Bean
    public OnlyofficeConverterRunner converterRunner(
            OnlyofficeConverterClient converterClient,
            List<OnlyofficeConverterUploader> converterFileUploaders
    ) {
        return new OnlyofficeDefaultConverterRunner(converterFileUploaders, converterClient);
    }
}
