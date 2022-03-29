package core.callback;

import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;

public interface OnlyofficeCallbackRegistry {
    /**
     *
     * @param callbacks
     */
    void registerCallbacks(OnlyofficeCallbackHandler... callbacks);

    /**
     *
     * @param statusCode
     */
    void removeCallbackByStatusCode(int statusCode);

    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     */
    void run(Callback callback) throws OnlyofficeCallbackRuntimeException;

    /**
     *
     * @return
     */
    int registeredCallbacks();
}
