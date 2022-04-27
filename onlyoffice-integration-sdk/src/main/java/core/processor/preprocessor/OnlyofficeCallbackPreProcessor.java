package core.processor.preprocessor;

import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import core.runner.implementation.CallbackRequest;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;

public abstract class OnlyofficeCallbackPreProcessor<S> {
    /**
     *
     * @param request
     */
    public void changeProcessors(CallbackRequest request) {};

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
    public final void run(CallbackRequest request) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (request == null)
            throw new OnlyofficeProcessBeforeRuntimeException("Expected to receive an instance of ConfigRequest. Got null");
        S validSchema = validateSchema(request.getPreProcessorSchema(preprocessorName()));
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
