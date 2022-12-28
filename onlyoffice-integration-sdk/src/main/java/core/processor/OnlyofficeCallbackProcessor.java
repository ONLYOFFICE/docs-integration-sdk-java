package core.processor;

import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackRegistry;
import core.util.OnlyofficeModelValidator;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class OnlyofficeCallbackProcessor implements OnlyofficeProcessor<Callback> {
    private final OnlyofficeCallbackRegistry callbackRegistry;

    /**
     *
     * @param callback
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void process(Callback callback) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (callback == null || callback.getStatus() == null)
            throw new OnlyofficeProcessRuntimeException("Callback object is null or has no status");

        OnlyofficeModelValidator.validate(callback);
        this.callbackRegistry.run(callback);
    }
}
