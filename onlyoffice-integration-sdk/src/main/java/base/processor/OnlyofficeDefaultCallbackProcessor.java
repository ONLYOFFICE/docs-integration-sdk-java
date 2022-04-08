package base.processor;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.registry.OnlyofficeCallbackRegistry;
import core.runner.callback.CallbackRequest;
import core.security.OnlyofficeJwtSecurity;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnlyofficeDefaultCallbackProcessor implements OnlyofficeCallbackProcessor {
    private final OnlyofficeCallbackRegistry callbackRegistry;
    private final OnlyofficeJwtSecurity jwtManager;

    /**
     *
     * @param request
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void process(CallbackRequest request) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (request == null || request.getCallback() == null || request.getCallback().getStatus() == null)
            throw new OnlyofficeProcessRuntimeException("Callback object is null or has no status");

        Callback callback = request.getCallback();
        String secret = callback.getSecret();
        String token = callback.getToken();
        if (secret != null && !secret.isBlank() && token != null && !token.isBlank()) {
            try {
                this.jwtManager.verify(callback, callback.getToken(), secret);
            } catch (OnlyofficeJwtVerificationRuntimeException e) {
                throw new OnlyofficeProcessRuntimeException(e.getMessage());
            }
        }
        this.callbackRegistry.run(callback);
    }
}
