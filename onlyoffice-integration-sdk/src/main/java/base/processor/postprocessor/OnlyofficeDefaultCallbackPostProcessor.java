package base.processor.postprocessor;

import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import core.processor.postprocessor.OnlyofficeCallbackPostProcessor;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessAfterRuntimeException;

import java.util.Map;
import java.util.UUID;

public class OnlyofficeDefaultCallbackPostProcessor extends OnlyofficeCallbackPostProcessor<Object> {
    public Object validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema) {
        return new Object();
    }

    public void processAfter(Callback model, Object validSchema) throws OnlyofficeProcessAfterRuntimeException, OnlyofficeInvalidParameterRuntimeException {}

    public String postprocessorName() {
        return UUID.randomUUID().toString();
    }
}
