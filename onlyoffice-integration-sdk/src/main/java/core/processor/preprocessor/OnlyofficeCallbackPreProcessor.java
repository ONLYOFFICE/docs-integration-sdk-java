package core.processor.preprocessor;

import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import core.runner.implementation.CallbackRequest;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;

import java.util.Map;

public abstract class OnlyofficeCallbackPreProcessor<S> {
    /**
     *
     * @param request
     */
    public void changeProcessors(CallbackRequest request) {};

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
    public final void run(CallbackRequest request) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (request == null)
            throw new OnlyofficeProcessBeforeRuntimeException("Expected to receive an instance of CallbackRequest. Got null");
        if (request.getCallback() == null)
            throw new OnlyofficeProcessBeforeRuntimeException("Expected to receive an instance of Callback. Got null");
        S validSchema = validateSchema(request.getCallback().getCustom(), request.getPreProcessorSchema(preprocessorName()));
        if (validSchema == null) return;
        processBefore(request.getCallback(), validSchema);
        changeProcessors(request);
    }

    /**
     *
     * @param callback
     * @param validSchema
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public abstract void processBefore(Callback callback, S validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @return
     */
    public abstract String preprocessorName();
}
