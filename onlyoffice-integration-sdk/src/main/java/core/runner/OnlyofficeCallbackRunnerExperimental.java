package core.runner;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.callback.Callback;
import exception.OnlyofficeCallbackRuntimeException;
import exception.OnlyofficeJwtSigningException;
import exception.OnlyofficeProcessAfterException;
import exception.OnlyofficeProcessBeforeException;

public class OnlyofficeCallbackRunnerExperimental implements OnlyofficeCallbackRunner {

    public void run(Callback callback) throws OnlyofficeProcessBeforeException, OnlyofficeProcessAfterException, OnlyofficeCallbackRuntimeException, JWTVerificationException, OnlyofficeJwtSigningException {

    }
}
