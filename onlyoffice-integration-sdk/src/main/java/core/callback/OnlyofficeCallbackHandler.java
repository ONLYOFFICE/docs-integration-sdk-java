package core.callback;

import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;

public interface OnlyofficeCallbackHandler {
    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     */
    void handle(Callback callback) throws OnlyofficeCallbackRuntimeException;

    /**
     *
     * @return
     */
    int getStatus();
}
