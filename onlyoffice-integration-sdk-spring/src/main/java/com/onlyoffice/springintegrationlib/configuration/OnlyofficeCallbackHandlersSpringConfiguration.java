package com.onlyoffice.springintegrationlib.configuration;

import base.handler.OnlyofficeForceSaveCallbackHandler;
import base.handler.OnlyofficeSaveCallbackHandler;
import base.uploader.OnlyofficeDefaultCallbackUploaderRunner;
import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import core.uploader.OnlyofficeUploader;
import core.uploader.OnlyofficeUploaderRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OnlyofficeCallbackHandlersSpringConfiguration {
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = Callback.class, parameterizedContainer = OnlyofficeUploader.class)
    @Bean
    public OnlyofficeUploaderRunner<Callback> callbackUploaderRunner(List<OnlyofficeUploader<Callback>> uploaders) {
        return new OnlyofficeDefaultCallbackUploaderRunner(uploaders);
    }

    @ConditionalOnBean(value = Callback.class, parameterizedContainer = OnlyofficeUploaderRunner.class)
    @Bean
    public OnlyofficeCallbackHandler callbackSaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeUploaderRunner<Callback> callbackUploaderRunner) {
        return new OnlyofficeSaveCallbackHandler(registry, callbackUploaderRunner);
    }

    @ConditionalOnBean(value = Callback.class, parameterizedContainer = OnlyofficeUploaderRunner.class)
    @Bean
    public OnlyofficeCallbackHandler callbackForcesaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeUploaderRunner<Callback> callbackUploaderRunner) {
        return new OnlyofficeForceSaveCallbackHandler(registry, callbackUploaderRunner);
    }
}
