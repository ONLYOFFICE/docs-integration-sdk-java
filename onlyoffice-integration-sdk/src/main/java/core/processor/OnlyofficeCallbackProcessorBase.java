package core.processor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.callback.OnlyofficeCallbackRegistry;
import core.model.callback.Callback;
import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeCallbackRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class OnlyofficeCallbackProcessorBase implements OnlyofficeCallbackProcessor {
    private final OnlyofficeCallbackRegistry registry;
    private final OnlyofficeProcessorCustomMapConfiguration configuration;
    private final OnlyofficeJwtManager jwtManager;

    /**
     *
     * @param callback
     * @throws OnlyofficeCallbackRuntimeException
     * @throws JWTVerificationException
     */
    public void handleCallback(Callback callback) throws OnlyofficeCallbackRuntimeException, JWTVerificationException {
        if (callback == null || callback.getStatus() == null) throw new OnlyofficeCallbackRuntimeException("Callback object is null or has no status");
        String secretMapKey = configuration.getSecretKey();
        Map<String, Object> custom = callback.getCustom();
        if (custom.containsKey(secretMapKey)) {
            Object secret = custom.get(secretMapKey);
            if (secret == null || !(secret instanceof String) || secret.toString().isBlank()) {
                this.registry.run(callback);
                return;
            }
            if (callback.getToken() == null || callback.getToken().isBlank()) {
                String tokenMapKey = configuration.getTokenKey();
                if (custom.containsKey(tokenMapKey)) {
                    Object token = custom.get(tokenMapKey);
                    if (token != null && token instanceof String && !token.toString().isBlank()) {
                        callback.setToken(token.toString());
                    }
                }
            }
            if (callback.getToken() != null && !callback.getToken().isBlank()) {
                this.jwtManager.verify(callback, callback.getToken(), secret.toString());
            }
        }
        this.registry.run(callback);
    }
}
