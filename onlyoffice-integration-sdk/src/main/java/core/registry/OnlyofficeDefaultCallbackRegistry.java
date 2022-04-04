package core.registry;

import core.model.callback.Callback;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import exception.OnlyofficeRegistryRuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnlyofficeDefaultCallbackRegistry implements OnlyofficeCallbackRegistry {
    private Map<Integer, OnlyofficeCallbackHandler> callbackHandlers = new ConcurrentHashMap<>();

    /**
     *
     * @param handlers
     */
    @Override
    public void register(OnlyofficeCallbackHandler... handlers) {
        for (OnlyofficeCallbackHandler handler : handlers) {
            if (this.callbackHandlers.containsKey(handler.getCode())) continue;
            this.callbackHandlers.put(handler.getCode(), handler);
        }
    }

    /**
     *
     * @param code
     */
    public void removeByCode(Integer code) {
        if (!this.callbackHandlers.containsKey(code)) return;
        this.callbackHandlers.remove(code);
    }

    /**
     *
     * @param callback
     * @throws OnlyofficeRegistryRuntimeException
     * @throws OnlyofficeRegistryHandlerRuntimeException
     */
    public void run(Callback callback) throws OnlyofficeRegistryRuntimeException, OnlyofficeRegistryHandlerRuntimeException {
        if (!this.callbackHandlers.containsKey(callback.getStatus())) return;
        this.callbackHandlers.get(callback.getStatus()).handle(callback);
    }

    /**
     *
     * @return
     */
    public int registered() {
        return this.callbackHandlers.size();
    }
}
