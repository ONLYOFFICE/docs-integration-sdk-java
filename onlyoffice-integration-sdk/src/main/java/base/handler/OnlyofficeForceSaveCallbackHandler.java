package base.handler;

import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackRegistry;
import core.registry.OnlyofficeCallbackHandler;
import core.runner.OnlyofficeCallbackUploaderRunner;
import exception.OnlyofficeCallbackRuntimeException;

public class OnlyofficeForceSaveCallbackHandler implements OnlyofficeCallbackHandler {
    private final OnlyofficeCallbackUploaderRunner callbackUploaderRunner;

    /**
     *
     * @param registry
     * @param callbackUploaderRunner
     */
    public OnlyofficeForceSaveCallbackHandler(
            OnlyofficeCallbackRegistry registry,
            OnlyofficeCallbackUploaderRunner callbackUploaderRunner
    ) {
        this.callbackUploaderRunner = callbackUploaderRunner;
        registry.register(this);
    }

    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     */
    public void handle(Callback callback) throws OnlyofficeCallbackRuntimeException {
        try {
            callbackUploaderRunner.run(callback);
        } catch (Exception e) {
            throw new OnlyofficeCallbackRuntimeException(e.getMessage());
        }
    }

    public Integer getCode() {
        return 6;
    }
}
