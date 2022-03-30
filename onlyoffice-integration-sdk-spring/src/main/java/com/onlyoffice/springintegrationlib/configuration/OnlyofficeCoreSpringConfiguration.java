package com.onlyoffice.springintegrationlib.configuration;

import client.OnlyofficeCommandClient;
import client.OnlyofficeConverterClient;
import core.OnlyofficeIntegrationSDK;
import core.callback.OnlyofficeCallbackRegistry;
import core.callback.OnlyofficeCallbackRegistryBase;
import core.model.callback.Callback;
import core.model.config.Config;
import core.processor.*;
import core.processor.implementation.OnlyofficeCallbackDefaultPreProcessor;
import core.processor.implementation.OnlyofficeEditorDefaultPreProcessor;
import core.processor.schema.OnlyofficeDefaultPrePostProcessorMapSchema;
import core.processor.schema.OnlyofficeProcessorCustomMapSchema;
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
    public OnlyofficeEditorProcessor onlyofficeEditorProcessor(
            OnlyofficeConfigUtil configUtil,
            OnlyofficeProcessorCustomMapSchema schema,
            OnlyofficeJwtManager jwtManager
    ) {
        return new OnlyofficeEditorProcessorBase(configUtil, schema, jwtManager);
    }

    @ConditionalOnProperty(prefix = "onlyoffice.defaults.preprocessors", name = "editor", havingValue = "enable")
    @Bean
    public OnlyofficePreProcessor<Config> defaultConfigPreProcessor(
            OnlyofficeDefaultPrePostProcessorMapSchema schema,
            OnlyofficeJwtManager jwtManager
    ) {
        return new OnlyofficeEditorDefaultPreProcessor(schema, jwtManager);
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
    public OnlyofficeCallbackProcessor onlyofficeCallbackProcessor(
            OnlyofficeCallbackRegistry registry,
            OnlyofficeProcessorCustomMapSchema schema,
            OnlyofficeJwtManager jwtManager
    ) {
        return new OnlyofficeCallbackProcessorBase(registry, schema, jwtManager);
    }

    @ConditionalOnProperty(prefix = "onlyoffice.defaults.preprocessors", name = "callback", havingValue = "enable")
    @Bean
    public OnlyofficePreProcessor<Callback> baseCallbackPreProcessor(
            OnlyofficeDefaultPrePostProcessorMapSchema schema,
            OnlyofficeJwtManager jwtManager
    ) {
        return new OnlyofficeCallbackDefaultPreProcessor(schema, jwtManager);
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

    @Bean
    public OnlyofficeIntegrationSDK integrationSDK(
            OnlyofficeCallbackRunner callbackRunner,
            OnlyofficeEditorRunner editorRunner,
            OnlyofficeCommandClient commandClient,
            OnlyofficeConverterClient converterClient
    ) {
        return new OnlyofficeIntegrationSDK(callbackRunner, editorRunner, commandClient, converterClient);
    }
}
