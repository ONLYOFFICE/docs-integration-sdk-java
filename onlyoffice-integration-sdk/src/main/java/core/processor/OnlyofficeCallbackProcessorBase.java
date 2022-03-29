package core.processor;

import core.callback.OnlyofficeCallbackRegistry;
import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OnlyofficeCallbackProcessorBase implements OnlyofficeCallbackProcessor {
    private final OnlyofficeCallbackRegistry registry;

    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     */
    public void handleCallback(Callback callback) throws OnlyofficeCallbackRuntimeException {
        if (callback == null || callback.getStatus() == null) throw new OnlyofficeCallbackRuntimeException("Callback object is null or has no status");
        this.registry.run(callback);
    }
}
