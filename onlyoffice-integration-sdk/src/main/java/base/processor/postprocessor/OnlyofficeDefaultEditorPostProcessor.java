package base.processor.postprocessor;

import com.google.common.collect.ImmutableMap;
import core.model.config.Config;
import core.processor.postprocessor.OnlyofficeEditorPostProcessor;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessAfterRuntimeException;

import java.util.UUID;

public class OnlyofficeDefaultEditorPostProcessor extends OnlyofficeEditorPostProcessor<Object> {
    public Object validateSchema(ImmutableMap<String, Object> schema) {
        return null;
    }

    public void processAfter(Config config, Object validSchema) throws OnlyofficeProcessAfterRuntimeException, OnlyofficeInvalidParameterRuntimeException {}

    public String postprocessorName() {
        return UUID.randomUUID().toString();
    }
}
