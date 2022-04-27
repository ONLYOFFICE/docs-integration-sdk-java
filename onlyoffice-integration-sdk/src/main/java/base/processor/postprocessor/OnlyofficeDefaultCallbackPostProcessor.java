package base.processor.postprocessor;

import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import core.processor.postprocessor.OnlyofficeCallbackPostProcessor;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessAfterRuntimeException;

import java.util.UUID;

public class OnlyofficeDefaultCallbackPostProcessor extends OnlyofficeCallbackPostProcessor<Object> {
    public Object validateSchema(ImmutableMap<String, Object> schema) {
        return null;
    }

    public void processAfter(Callback model, Object validSchema) throws OnlyofficeProcessAfterRuntimeException, OnlyofficeInvalidParameterRuntimeException {}

    public String postprocessorName() {
        return UUID.randomUUID().toString();
    }
}
