package com.onlyoffice.springintegrationlib.configuration;

import base.processor.OnlyofficeDefaultCallbackProcessor;
import base.processor.OnlyofficeDefaultEditorProcessor;
import base.processor.post.OnlyofficeDefaultCallbackPostProcessor;
import base.processor.post.OnlyofficeDefaultEditorPostProcessor;
import base.processor.pre.OnlyofficeDefaultCallbackPreProcessor;
import base.processor.pre.OnlyofficeDefaultEditorPreProcessor;
import base.runner.callback.OnlyofficeDefaultCallbackRunner;
import base.runner.editor.OnlyofficeDefaultEditorRunner;
import core.OnlyofficeIntegrationSDK;
import core.client.OnlyofficeCommandClient;
import core.client.OnlyofficeConverterClient;
import core.model.callback.Callback;
import core.model.config.Config;
import core.model.converter.request.ConverterRequest;
import core.processor.OnlyofficePostProcessor;
import core.processor.OnlyofficePreProcessor;
import core.processor.OnlyofficeProcessor;
import core.registry.OnlyofficeCallbackRegistry;
import core.registry.OnlyofficeDefaultCallbackRegistry;
import core.runner.OnlyofficeRunner;
import core.security.OnlyofficeJwtSecurity;
import core.util.OnlyofficeConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class OnlyofficeCoreSpringConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackRegistry onlyofficeCallbackRegistry() {
        return new OnlyofficeDefaultCallbackRegistry();
    }

    @ConditionalOnMissingBean(value = Config.class, parameterizedContainer = OnlyofficeProcessor.class)
    @Bean
    public OnlyofficeProcessor<Config> onlyofficeEditorProcessor(
            OnlyofficeConfig configUtil,
            OnlyofficeJwtSecurity jwtManager
    ) {
        return new OnlyofficeDefaultEditorProcessor(configUtil, jwtManager);
    }

    @Bean
    public OnlyofficePreProcessor<Config> defaultConfigPreProcessor(OnlyofficeJwtSecurity jwtManager) {
        return new OnlyofficeDefaultEditorPreProcessor(jwtManager);
    }

    @ConditionalOnMissingBean(value = Config.class, parameterizedContainer = OnlyofficePreProcessor.class)
    @Bean
    public OnlyofficePreProcessor<Configuration> emptyConfigOnlyofficePreProcessor() {
        return new OnlyofficePreProcessor<Configuration>() {
            @Override
            public String preprocessorName() {
                return UUID.randomUUID().toString();
            }
        };
    }

    @ConditionalOnMissingBean(value = Config.class, parameterizedContainer = OnlyofficePostProcessor.class)
    @Bean
    public OnlyofficePostProcessor<Config> emptyConfigOnlyofficePostProcessor() {
        return new OnlyofficeDefaultEditorPostProcessor();
    }

    @ConditionalOnMissingBean(value = Callback.class, parameterizedContainer = OnlyofficeProcessor.class)
    @Bean
    public OnlyofficeProcessor<Callback> onlyofficeCallbackProcessor(
            OnlyofficeCallbackRegistry registry,
            OnlyofficeJwtSecurity jwtManager
    ) {
        return new OnlyofficeDefaultCallbackProcessor(registry, jwtManager);
    }

    @Bean
    public OnlyofficePreProcessor<Callback> baseCallbackPreProcessor(OnlyofficeJwtSecurity jwtManager) {
        return new OnlyofficeDefaultCallbackPreProcessor(jwtManager);
    }

    @ConditionalOnMissingBean(value = Callback.class, parameterizedContainer = OnlyofficePreProcessor.class)
    @Bean
    public OnlyofficePreProcessor<Callback> emptyCallbackPreProcessor() {
        return new OnlyofficePreProcessor<Callback>() {
            @Override
            public String preprocessorName() {
                return UUID.randomUUID().toString();
            }
        };
    }

    @ConditionalOnMissingBean(value = Callback.class, parameterizedContainer = OnlyofficePostProcessor.class)
    @Bean
    public OnlyofficePostProcessor<Callback> emptyCallbackPostProcessor() {
        return new OnlyofficeDefaultCallbackPostProcessor();
    }

    @ConditionalOnMissingBean(value = Config.class, parameterizedContainer = OnlyofficeRunner.class)
    @Bean
    public OnlyofficeRunner<Config> onlyofficeEditorRunner(
            OnlyofficeProcessor<Config> editorProcessor,
            List<OnlyofficePreProcessor<Config>> editorPreProcessors,
            List<OnlyofficePostProcessor<Config>> editorPostProcessors
    ) {
        return new OnlyofficeDefaultEditorRunner(editorProcessor, editorPreProcessors, editorPostProcessors);
    }

    @ConditionalOnMissingBean(value = Callback.class, parameterizedContainer = OnlyofficeRunner.class)
    @Bean
    public OnlyofficeRunner<Callback> onlyofficeCallbackRunner(
            OnlyofficeProcessor<Callback> callbackProcessor,
            List<OnlyofficePreProcessor<Callback>> callbackPreProcessors,
            List<OnlyofficePostProcessor<Callback>> callbackPostProcessors
    ) {
        return new OnlyofficeDefaultCallbackRunner(callbackProcessor, callbackPreProcessors, callbackPostProcessors);
    }

    @Bean
    public OnlyofficeIntegrationSDK integrationSDK(
            OnlyofficeRunner<Callback> callbackRunner,
            OnlyofficeRunner<Config> editorRunner,
            OnlyofficeRunner<ConverterRequest> converterRunner,
            OnlyofficeCommandClient commandClient,
            OnlyofficeConverterClient converterClient
    ) {
        return new OnlyofficeIntegrationSDK(callbackRunner, editorRunner, converterRunner, commandClient, converterClient);
    }
}
