package base.processor;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.registry.OnlyofficeCallbackRegistry;
import core.security.OnlyofficeJwtSecurity;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OnlyofficeDefaultCallbackProcessor implements OnlyofficeCallbackProcessor {
    private final OnlyofficeCallbackRegistry callbackRegistry;
    private final OnlyofficeJwtSecurity jwtManager;

    /**
     *
     * @param callback
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void process(Callback callback) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (callback == null || callback.getStatus() == null)
            throw new OnlyofficeProcessRuntimeException("Callback object is null or has no status");
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
