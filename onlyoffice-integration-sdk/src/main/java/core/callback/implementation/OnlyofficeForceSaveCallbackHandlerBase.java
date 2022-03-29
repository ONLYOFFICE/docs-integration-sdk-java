package core.callback.implementation;

import core.callback.OnlyofficeCallbackHandler;
import core.callback.OnlyofficeCallbackRegistry;
import core.callback.uploader.OnlyofficeCallbackUploaderRunner;
import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;

public class OnlyofficeForceSaveCallbackHandlerBase implements OnlyofficeCallbackHandler {
    private final OnlyofficeCallbackUploaderRunner callbackUploaderRunner;

    /**
     *
     * @param registry
     * @param callbackUploaderRunner
     */
    public OnlyofficeForceSaveCallbackHandlerBase(OnlyofficeCallbackRegistry registry, OnlyofficeCallbackUploaderRunner callbackUploaderRunner) {
        this.callbackUploaderRunner = callbackUploaderRunner;
        registry.registerCallbacks(this);
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

    /**
     *
     * @return
     */
    public int getStatus() {
        return 6;
    }
}
