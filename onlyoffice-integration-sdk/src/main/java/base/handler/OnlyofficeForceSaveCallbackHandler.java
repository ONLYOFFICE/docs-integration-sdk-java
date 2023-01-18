package base.handler;

import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackHandler;
import core.uploader.OnlyofficeUploaderRunner;
import exception.OnlyofficeCallbackRuntimeException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnlyofficeForceSaveCallbackHandler implements OnlyofficeCallbackHandler {
    private final OnlyofficeUploaderRunner<Callback> callbackUploaderRunner;

    /**
     *
     * @param callbackUploaderRunner
     */
    @Inject
    public OnlyofficeForceSaveCallbackHandler(OnlyofficeUploaderRunner<Callback> callbackUploaderRunner) {
        this.callbackUploaderRunner = callbackUploaderRunner;
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
