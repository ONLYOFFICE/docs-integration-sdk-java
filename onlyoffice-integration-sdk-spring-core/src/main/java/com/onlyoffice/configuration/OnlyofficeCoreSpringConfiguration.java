package com.onlyoffice.configuration;

import base.processor.OnlyofficeDefaultCallbackProcessor;
import base.processor.OnlyofficeDefaultEditorProcessor;
import base.processor.post.OnlyofficeDefaultCallbackPostProcessor;
import base.processor.post.OnlyofficeDefaultEditorPostProcessor;
import base.processor.pre.OnlyofficeDefaultCallbackPreProcessor;
import base.processor.pre.OnlyofficeDefaultEditorPreProcessor;
import base.registry.OnlyofficeDefaultCallbackRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.OnlyofficeIntegrationSDK;
import core.client.OnlyofficeCommandClient;
import core.client.OnlyofficeConverterClient;
import core.model.converter.request.ConverterRequest;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.post.OnlyofficeCallbackPostProcessor;
import core.processor.post.OnlyofficeEditorPostProcessor;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.registry.OnlyofficeCallbackRegistry;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.OnlyofficeEditorRunner;
import core.runner.callback.OnlyofficeCustomizableCallbackRunner;
import core.runner.callback.OnlyofficeSequentialCallbackRunner;
import core.runner.editor.OnlyofficeCustomizableEditorRunner;
import core.runner.editor.OnlyofficeSequentialEditorRunner;
import core.security.OnlyofficeJwtSecurity;
import core.uploader.OnlyofficeUploaderRunner;
import core.util.OnlyofficeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class OnlyofficeCoreSpringConfiguration {
    @Value("${onlyoffice.runner.editor.customizable:disable}")
    private String customizableEditor;
    @Value("${onlyoffice.runner.callback.customizable:disable}")
    private String customizableCallback;

    @Bean
    public OnlyofficeCallbackRegistry onlyofficeCallbackRegistry() {
        return new OnlyofficeDefaultCallbackRegistry();
    }

    @Bean
    public OnlyofficeEditorProcessor onlyofficeEditorProcessor(
            OnlyofficeConfig configUtil,
            OnlyofficeJwtSecurity jwtManager
    ) {
        return new OnlyofficeDefaultEditorProcessor(configUtil, jwtManager);
    }

    @Bean
    @Order(0)
    public OnlyofficeEditorPreProcessor defaultConfigPreProcessor(
            ObjectMapper objectMapper
    ) {
        return new OnlyofficeDefaultEditorPreProcessor(objectMapper);
    }

    @Bean
    public OnlyofficeEditorPreProcessor emptyConfigOnlyofficePreProcessor() {
        return new OnlyofficeEditorPreProcessor() {
            public String preprocessorName() {
                return UUID.randomUUID().toString();
            }
        };
    }

    @Bean
    public OnlyofficeEditorPostProcessor emptyConfigOnlyofficePostProcessor() {
        return new OnlyofficeDefaultEditorPostProcessor();
    }

    @Bean
    public OnlyofficeCallbackProcessor onlyofficeCallbackProcessor(
            OnlyofficeCallbackRegistry registry,
            OnlyofficeJwtSecurity jwtManager
    ) {
        return new OnlyofficeDefaultCallbackProcessor(registry, jwtManager);
    }

    @Bean
    @Order(0)
    public OnlyofficeCallbackPreProcessor baseCallbackPreProcessor(
            ObjectMapper objectMapper
    ) {
        return new OnlyofficeDefaultCallbackPreProcessor(objectMapper);
    }

    @Bean
    public OnlyofficeCallbackPreProcessor emptyCallbackPreProcessor() {
        return new OnlyofficeCallbackPreProcessor() {
            public String preprocessorName() {
                return UUID.randomUUID().toString();
            }
        };
    }

    @Bean
    public OnlyofficeCallbackPostProcessor emptyCallbackPostProcessor() {
        return new OnlyofficeDefaultCallbackPostProcessor();
    }

    @Bean
    public OnlyofficeEditorRunner onlyofficeEditorRunner(
            OnlyofficeEditorProcessor editorProcessor,
            List<OnlyofficeEditorPreProcessor> editorPreProcessors,
            List<OnlyofficeEditorPostProcessor> editorPostProcessors
    ) {
        if (customizableEditor.equals("enable")) {
            Map<String, OnlyofficeEditorPreProcessor> preProcessors = editorPreProcessors.stream()
                    .collect(Collectors.toMap(OnlyofficeEditorPreProcessor::preprocessorName, Function.identity()));
            Map<String, OnlyofficeEditorPostProcessor> postProcessors = editorPostProcessors.stream()
                    .collect(Collectors.toMap(OnlyofficeEditorPostProcessor::postprocessorName, Function.identity()));
            return new OnlyofficeCustomizableEditorRunner(editorProcessor, preProcessors, postProcessors);
        }
        return new OnlyofficeSequentialEditorRunner(editorProcessor, editorPreProcessors, editorPostProcessors);
    }

    @Bean
    public OnlyofficeCallbackRunner onlyofficeCallbackRunner(
            OnlyofficeCallbackProcessor callbackProcessor,
            List<OnlyofficeCallbackPreProcessor> callbackPreProcessors,
            List<OnlyofficeCallbackPostProcessor> callbackPostProcessors
    ) {
        if (customizableCallback.equals("enable")) {
            Map<String, OnlyofficeCallbackPreProcessor> preProcessors = callbackPreProcessors.stream()
                    .collect(Collectors.toMap(OnlyofficeCallbackPreProcessor::preprocessorName, Function.identity()));
            Map<String, OnlyofficeCallbackPostProcessor> postProcessors = callbackPostProcessors.stream()
                    .collect(Collectors.toMap(OnlyofficeCallbackPostProcessor::postprocessorName, Function.identity()));
            return new OnlyofficeCustomizableCallbackRunner(callbackProcessor, preProcessors, postProcessors);
        }
        return new OnlyofficeSequentialCallbackRunner(callbackProcessor, callbackPreProcessors, callbackPostProcessors);
    }

    @Bean
    public OnlyofficeIntegrationSDK integrationSDK(
            OnlyofficeCallbackRunner callbackRunner,
            OnlyofficeEditorRunner editorRunner,
            OnlyofficeUploaderRunner<ConverterRequest> converterRunner,
            OnlyofficeCommandClient commandClient,
            OnlyofficeConverterClient converterClient
    ) {
        return new OnlyofficeIntegrationSDK(callbackRunner, editorRunner, converterRunner, commandClient, converterClient);
    }
}
