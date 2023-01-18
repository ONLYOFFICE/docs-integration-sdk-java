package base.registry;

import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import exception.OnlyofficeRegistryRuntimeException;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class OnlyofficeDefaultCallbackRegistry implements OnlyofficeCallbackRegistry {
    private Map<Integer, OnlyofficeCallbackHandler> callbackHandlers = new ConcurrentHashMap<>();

    /**
     *
     * @param handlers
     */
    public void register(OnlyofficeCallbackHandler... handlers) {
        if (handlers == null) return;
        for (OnlyofficeCallbackHandler handler : handlers) {
            if (handler == null || this.callbackHandlers.containsKey(handler.getCode())) continue;
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
