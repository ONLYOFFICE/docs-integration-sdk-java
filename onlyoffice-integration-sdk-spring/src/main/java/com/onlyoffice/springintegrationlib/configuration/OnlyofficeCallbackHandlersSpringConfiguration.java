package com.onlyoffice.springintegrationlib.configuration;

import base.handler.OnlyofficeForceSaveCallbackHandler;
import base.handler.OnlyofficeSaveCallbackHandler;
import base.runner.OnlyofficeDefaultCallbackUploaderRunner;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import core.runner.OnlyofficeCallbackUploaderRunner;
import core.uploader.OnlyofficeCallbackUploader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OnlyofficeCallbackHandlersSpringConfiguration {
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = OnlyofficeCallbackUploader.class)
    @Bean
    public OnlyofficeCallbackUploaderRunner callbackUploaderRunner(List<OnlyofficeCallbackUploader> uploaders) {
        return new OnlyofficeDefaultCallbackUploaderRunner(uploaders);
    }

    @ConditionalOnBean(OnlyofficeCallbackUploaderRunner.class)
    @Bean
    public OnlyofficeCallbackHandler callbackSaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeCallbackUploaderRunner callbackUploaderRunner) {
        return new OnlyofficeSaveCallbackHandler(registry, callbackUploaderRunner);
    }

    @ConditionalOnBean(OnlyofficeCallbackUploaderRunner.class)
    @Bean
    public OnlyofficeCallbackHandler callbackForcesaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeCallbackUploaderRunner callbackUploaderRunner) {
        return new OnlyofficeForceSaveCallbackHandler(registry, callbackUploaderRunner);
    }
}
