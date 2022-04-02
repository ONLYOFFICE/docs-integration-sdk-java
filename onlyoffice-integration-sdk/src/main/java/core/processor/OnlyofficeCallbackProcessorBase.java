package core.processor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.callback.OnlyofficeCallbackRegistry;
import core.model.callback.Callback;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeCallbackRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OnlyofficeCallbackProcessorBase implements OnlyofficeCallbackProcessor {
    private final OnlyofficeCallbackRegistry registry;
    private final OnlyofficeJwtManager jwtManager;

    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     * @throws JWTVerificationException
     */
    public void handleCallback(Callback callback) throws OnlyofficeCallbackRuntimeException, JWTVerificationException {
        if (callback == null || callback.getStatus() == null)
            throw new OnlyofficeCallbackRuntimeException("Callback object is null or has no status");
        String secret = callback.getSecret();
        String token = callback.getToken();
        if (secret != null && !secret.isBlank() && token != null && !token.isBlank())
            this.jwtManager.verify(callback, callback.getToken(), secret);
        this.registry.run(callback);
    }
}
