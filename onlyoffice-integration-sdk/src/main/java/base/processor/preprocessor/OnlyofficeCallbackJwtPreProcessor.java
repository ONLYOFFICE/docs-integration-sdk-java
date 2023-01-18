package base.processor.preprocessor;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackPreProcessor;
import core.security.OnlyofficeJwtSecurity;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnlyofficeCallbackJwtPreProcessor implements OnlyofficeCallbackPreProcessor {
    private final OnlyofficeJwtSecurity jwtManager;
    private String jwtSecret;

    /**
     *
     * @param callback
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void processBefore(Callback callback) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        try {
            if (callback.getToken() != null && !callback.getToken().isBlank() && jwtSecret != null && !jwtSecret.isBlank())
                this.jwtManager.verify(callback, callback.getToken(), jwtSecret);
        } catch (Exception e) {
            throw new OnlyofficeProcessBeforeRuntimeException(e.getMessage(), e);
        }
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
}