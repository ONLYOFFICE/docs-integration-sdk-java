package core.callback;

import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnlyofficeCallbackRegistryBase implements OnlyofficeCallbackRegistry {
    private Map<Integer, OnlyofficeCallbackHandler> callbackHandlers = new ConcurrentHashMap<>();

    /**
     *
     * @param callbacks
     */
    public void registerCallbacks(OnlyofficeCallbackHandler... callbacks) {
        for (OnlyofficeCallbackHandler callback : callbacks) {
            if (this.callbackHandlers.containsKey(callback.getStatus())) continue;
            this.callbackHandlers.put(callback.getStatus(), callback);
        }
    }

    /**
     *
     * @param statusCode
     */
    public void removeCallbackByStatusCode(int statusCode) {
        this.callbackHandlers.remove(statusCode);
    }

    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     */
    public void run(Callback callback) throws OnlyofficeCallbackRuntimeException {
        OnlyofficeCallbackHandler handler = this.callbackHandlers.get(callback.getStatus());
        if (handler != null) handler.handle(callback);
    }

    /**
     *
     * @return
     */
    public int registeredCallbacks() {
        return this.callbackHandlers.size();
    }
}
