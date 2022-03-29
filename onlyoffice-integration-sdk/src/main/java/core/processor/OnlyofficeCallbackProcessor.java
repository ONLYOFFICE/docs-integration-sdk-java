package core.processor;

import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;

public interface OnlyofficeCallbackProcessor {
    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     */
    void handleCallback(Callback callback) throws OnlyofficeCallbackRuntimeException;
}
