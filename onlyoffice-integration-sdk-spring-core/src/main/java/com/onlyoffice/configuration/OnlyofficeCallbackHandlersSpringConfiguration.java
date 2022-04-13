package com.onlyoffice.configuration;

import base.handler.OnlyofficeForceSaveCallbackHandler;
import base.handler.OnlyofficeSaveCallbackHandler;
import base.uploader.OnlyofficeDefaultCallbackUploaderRunner;
import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import core.uploader.OnlyofficeUploader;
import core.uploader.OnlyofficeUploaderRunner;
import exception.OnlyofficeUploaderRuntimeException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class OnlyofficeCallbackHandlersSpringConfiguration {
    @Bean
    public OnlyofficeUploader<Callback> emptyCallbackUploader() {
        return new OnlyofficeUploader<Callback>() {
            public void upload(Callback callback, InputStream inputStream) throws OnlyofficeUploaderRuntimeException {}
        };
    }

    @Bean
    public OnlyofficeUploaderRunner<Callback> callbackUploaderRunner(List<OnlyofficeUploader<Callback>> uploaders) {
        return new OnlyofficeDefaultCallbackUploaderRunner(uploaders);
    }

    @Bean
    public OnlyofficeCallbackHandler callbackSaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeUploaderRunner<Callback> callbackUploaderRunner) {
        return new OnlyofficeSaveCallbackHandler(registry, callbackUploaderRunner);
    }

    @Bean
    public OnlyofficeCallbackHandler callbackForcesaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeUploaderRunner<Callback> callbackUploaderRunner) {
        return new OnlyofficeForceSaveCallbackHandler(registry, callbackUploaderRunner);
    }
}
