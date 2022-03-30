package core.processor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;

public interface OnlyofficeCallbackProcessor {
    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     * @throws JWTVerificationException
     */
    void handleCallback(Callback callback) throws OnlyofficeCallbackRuntimeException, JWTVerificationException;
}
