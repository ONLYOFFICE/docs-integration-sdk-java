package com.onlyoffice.springintegrationlib.configuration;

import core.callback.OnlyofficeCallbackHandler;
import core.callback.OnlyofficeCallbackRegistry;
import core.callback.implementation.OnlyofficeForceSaveCallbackHandlerBase;
import core.callback.implementation.OnlyofficeSaveCallbackHandlerBase;
import core.callback.uploader.OnlyofficeCallbackUploader;
import core.callback.uploader.OnlyofficeCallbackUploaderRunner;
import core.callback.uploader.OnlyofficeCallbackUploaderRunnerBase;
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
    public OnlyofficeCallbackUploaderRunner uploaderRunner(List<OnlyofficeCallbackUploader> uploaders) {
        return new OnlyofficeCallbackUploaderRunnerBase(uploaders);
    }

    @ConditionalOnBean(OnlyofficeCallbackUploaderRunner.class)
    @Bean
    public OnlyofficeCallbackHandler callbackSaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeCallbackUploaderRunner callbackUploaderRunner) {
        return new OnlyofficeSaveCallbackHandlerBase(registry, callbackUploaderRunner);
    }

    @ConditionalOnBean(OnlyofficeCallbackUploaderRunner.class)
    @Bean
    public OnlyofficeCallbackHandler callbackForcesaveHandler(OnlyofficeCallbackRegistry registry, OnlyofficeCallbackUploaderRunner callbackUploaderRunner) {
        return new OnlyofficeForceSaveCallbackHandlerBase(registry, callbackUploaderRunner);
    }
}
