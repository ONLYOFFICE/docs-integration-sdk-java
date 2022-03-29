package core.runner;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;
import exception.OnlyofficeJwtSigningException;
import exception.OnlyofficeProcessAfterException;
import exception.OnlyofficeProcessBeforeException;

public interface OnlyofficeCallbackRunner {
    /**
     *
     * @param callback
     * @throws OnlyofficeProcessBeforeException
     * @throws OnlyofficeProcessAfterException
     * @throws OnlyofficeCallbackRuntimeException
     * @throws JWTVerificationException
     * @throws OnlyofficeJwtSigningException
     */
    void run(Callback callback) throws OnlyofficeProcessBeforeException, OnlyofficeProcessAfterException, OnlyofficeCallbackRuntimeException, JWTVerificationException, OnlyofficeJwtSigningException;
}