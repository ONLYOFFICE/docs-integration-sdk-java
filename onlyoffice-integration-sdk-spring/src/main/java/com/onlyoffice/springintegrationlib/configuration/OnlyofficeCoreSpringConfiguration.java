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
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.post.OnlyofficeCallbackPostProcessor;
import core.processor.post.OnlyofficeEditorPostProcessor;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.registry.OnlyofficeCallbackRegistry;
import core.registry.OnlyofficeDefaultCallbackRegistry;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.OnlyofficeConverterRunner;
import core.runner.OnlyofficeEditorRunner;
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

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeEditorProcessor onlyofficeEditorProcessor(
            OnlyofficeConfig configUtil,
            OnlyofficeJwtSecurity jwtManager
    ) {
        return new OnlyofficeDefaultEditorProcessor(configUtil, jwtManager);
    }

    @Bean
    public OnlyofficeEditorPreProcessor defaultConfigPreProcessor(OnlyofficeJwtSecurity jwtManager) {
        return new OnlyofficeDefaultEditorPreProcessor(jwtManager);
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeEditorPreProcessor emptyConfigOnlyofficePreProcessor() {
        return new OnlyofficeEditorPreProcessor() {
            @Override
            public String preprocessorName() {
                return UUID.randomUUID().toString();
            }
        };
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeEditorPostProcessor emptyConfigOnlyofficePostProcessor() {
        return new OnlyofficeDefaultEditorPostProcessor();
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackProcessor onlyofficeCallbackProcessor(
            OnlyofficeCallbackRegistry registry,
            OnlyofficeJwtSecurity jwtManager
    ) {
        return new OnlyofficeDefaultCallbackProcessor(registry, jwtManager);
    }

    @Bean
    public OnlyofficeCallbackPreProcessor baseCallbackPreProcessor(OnlyofficeJwtSecurity jwtManager) {
        return new OnlyofficeDefaultCallbackPreProcessor(jwtManager);
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackPreProcessor emptyCallbackPreProcessor() {
        return new OnlyofficeCallbackPreProcessor() {
            @Override
            public String preprocessorName() {
                return UUID.randomUUID().toString();
            }
        };
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackPostProcessor emptyCallbackPostProcessor() {
        return new OnlyofficeDefaultCallbackPostProcessor();
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeEditorRunner onlyofficeEditorRunner(
            OnlyofficeEditorProcessor editorProcessor,
            List<OnlyofficeEditorPreProcessor> editorPreProcessors,
            List<OnlyofficeEditorPostProcessor> editorPostProcessors
    ) {
        return new OnlyofficeDefaultEditorRunner(editorProcessor, editorPreProcessors, editorPostProcessors);
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeCallbackRunner onlyofficeCallbackRunner(
            OnlyofficeCallbackProcessor callbackProcessor,
            List<OnlyofficeCallbackPreProcessor> callbackPreProcessors,
            List<OnlyofficeCallbackPostProcessor> callbackPostProcessors
    ) {
        return new OnlyofficeDefaultCallbackRunner(callbackProcessor, callbackPreProcessors, callbackPostProcessors);
    }

    @Bean
    public OnlyofficeIntegrationSDK integrationSDK(
            OnlyofficeCallbackRunner callbackRunner,
            OnlyofficeEditorRunner editorRunner,
            OnlyofficeConverterRunner converterRunner,
            OnlyofficeCommandClient commandClient,
            OnlyofficeConverterClient converterClient
    ) {
        return new OnlyofficeIntegrationSDK(callbackRunner, editorRunner, converterRunner, commandClient, converterClient);
    }
}
