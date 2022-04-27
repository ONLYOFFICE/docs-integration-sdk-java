package core.processor.preprocessor;

import com.google.common.collect.ImmutableMap;
import core.model.config.Config;
import core.runner.implementation.ConfigRequest;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;

public abstract class OnlyofficeEditorPreProcessor<S> {
    /**
     *
     * @param request
     */
    public void changeProcessors(ConfigRequest request) {};

    /**
     *
     * @param schema
     * @return
     */
    public abstract S validateSchema(ImmutableMap<String, Object> schema);

    /**
     *
     * @param request
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public final void run(ConfigRequest request) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (request == null)
            throw new OnlyofficeProcessBeforeRuntimeException("Expected to receive an instance of ConfigRequest. Got null");
        S validSchema = validateSchema(request.getPreProcessorSchema(preprocessorName()));
        if (validSchema == null) return;
        processBefore(request.getConfig(), validSchema);
        changeProcessors(request);
    }

    /**
     *
     * @param config
     * @param validSchema
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public abstract void processBefore(Config config, S validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @return
     */
    public abstract String preprocessorName();
}
