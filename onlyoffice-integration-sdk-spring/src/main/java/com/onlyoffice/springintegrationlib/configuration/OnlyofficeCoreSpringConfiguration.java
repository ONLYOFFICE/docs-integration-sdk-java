package com.onlyoffice.springintegrationlib.configuration;

import core.callback.OnlyofficeCallbackRegistry;
import core.callback.OnlyofficeCallbackRegistryBase;
import core.model.callback.Callback;
import core.model.config.Config;
import core.processor.*;
import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import core.processor.implementation.OnlyofficeCallbackPreProcessorBase;
import core.processor.implementation.OnlyofficeEditorPostProcessorBase;
import core.processor.implementation.OnlyofficeEditorPreProcessorBase;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.OnlyofficeCallbackRunnerBase;
import core.runner.OnlyofficeEditorRunner;
import core.runner.OnlyofficeEditorRunnerBase;
import core.security.OnlyofficeJwtManager;
import core.util.OnlyofficeConfigUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class OnlyofficeCoreSpringConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackRegistry onlyofficeCallbackRegistry() {
        return new OnlyofficeCallbackRegistryBase();
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeEditorProcessor onlyofficeEditorProcessor(OnlyofficeConfigUtil configUtil) {
        return new OnlyofficeEditorProcessorBase(configUtil);
    }

    @ConditionalOnProperty(prefix = "onlyoffice.preprocessors", name = "default", havingValue = "enable")
    @Bean
    @Order(value = 0)
    public OnlyofficePreProcessor<Config> baseConfigPreProcessor(
            OnlyofficeProcessorCustomMapConfiguration configuration,
            OnlyofficeJwtManager jwtManager
    ) {
        return new OnlyofficeEditorPreProcessorBase(configuration, jwtManager);
    }

    @ConditionalOnProperty(prefix = "onlyoffice.postprocessors", name = "default", havingValue = "enable")
    @Bean
    @Order(value = 0)
    public OnlyofficePostProcessor<Config> baseConfigPostProcessor(
            OnlyofficeProcessorCustomMapConfiguration configuration,
            OnlyofficeJwtManager jwtManager
    ) {
        return new OnlyofficeEditorPostProcessorBase(configuration, jwtManager);
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficePreProcessor<Config> emptyConfigOnlyofficePreProcessor() {
        return new OnlyofficePreProcessor<Config>(){};
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficePostProcessor<Config> emptyConfigOnlyofficePostProcessor() {
        return new OnlyofficePostProcessor<Config>() {};
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackProcessor onlyofficeCallbackProcessor(OnlyofficeCallbackRegistry registry) {
        return new OnlyofficeCallbackProcessorBase(registry);
    }

    @ConditionalOnProperty(prefix = "onlyoffice.preprocessors", name = "default", havingValue = "enable")
    @Bean
    @Order(value = 0)
    public OnlyofficePreProcessor<Callback> baseCallbackPreProcessor(
            OnlyofficeProcessorCustomMapConfiguration configuration,
            OnlyofficeJwtManager jwtManager
    ) {
        return new OnlyofficeCallbackPreProcessorBase(configuration, jwtManager);
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficePreProcessor<Callback> emptyCallbackPreProcessor() {
        return new OnlyofficePreProcessor<Callback>() {};
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficePostProcessor<Callback> emptyCallbackPostProcessor() {
        return new OnlyofficePostProcessor<Callback>() {};
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeEditorRunner onlyofficeEditorRunner(
            OnlyofficeEditorProcessor editorProcessor,
            List<OnlyofficePreProcessor<Config>> editorPreProcessors,
            List<OnlyofficePostProcessor<Config>> editorPostProcessors
    ) {
        return new OnlyofficeEditorRunnerBase(editorProcessor, editorPreProcessors, editorPostProcessors);
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackRunner onlyofficeCallbackRunner(
            OnlyofficeCallbackProcessor callbackProcessor,
            List<OnlyofficePreProcessor<Callback>> callbackPreProcessors,
            List<OnlyofficePostProcessor<Callback>> callbackPostProcessors
    ) {
        return new OnlyofficeCallbackRunnerBase(callbackProcessor, callbackPreProcessors, callbackPostProcessors);
    }
}
