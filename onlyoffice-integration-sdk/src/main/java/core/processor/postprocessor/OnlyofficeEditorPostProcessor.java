package core.processor.postprocessor;

import com.google.common.collect.ImmutableMap;
import core.model.config.Config;
import core.runner.implementation.ConfigRequest;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;

import java.util.Map;

public abstract class OnlyofficeEditorPostProcessor<S> {
    /**
     *
     * @param request
     */
    public void changeProcessors(ConfigRequest request) {};

    /**
     *
     * @param customData
     * @param schema
     * @return
     */
    public abstract S validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema);

    /**
     *
     * @param request
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public final void run(ConfigRequest request) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (request == null)
            throw new OnlyofficeProcessBeforeRuntimeException("Expected to receive an instance of ConfigRequest. Got null");
        if (request.getConfig() == null)
            throw new OnlyofficeProcessBeforeRuntimeException("Expected to receive an instance of Config. Got null");
        S validSchema = validateSchema(request.getConfig().getCustom(), request.getPostProcessorSchema(postprocessorName()));
        if (validSchema == null) return;
        processAfter(request.getConfig(), validSchema);
        changeProcessors(request);
    }

    /**
     *
     * @param config
     * @param validSchema
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public abstract void processAfter(Config config, S validSchema) throws OnlyofficeProcessAfterRuntimeException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @return
     */
    public abstract String postprocessorName();
}
