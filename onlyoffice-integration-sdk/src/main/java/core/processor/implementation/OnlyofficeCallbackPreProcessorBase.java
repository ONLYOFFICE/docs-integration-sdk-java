package core.processor.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.callback.Callback;
import core.processor.OnlyofficePreProcessor;
import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeProcessBeforeException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeCallbackPreProcessorBase implements OnlyofficePreProcessor<Callback> {
    private final OnlyofficeProcessorCustomMapConfiguration configuration;
    private final OnlyofficeJwtManager jwtManager;

    public void processBefore(Callback callback) throws OnlyofficeProcessBeforeException, JWTVerificationException {
        if (callback == null) return;

        Map<String, Object> custom = callback.getCustom();

        String beforeMapKey = this.configuration.getBeforeMapKey();
        if (!custom.containsKey(beforeMapKey)) return;
        if (!(custom.get(beforeMapKey) instanceof Map)) return;

        Map<String, Object> jwtMap;
        try {
            jwtMap = (Map<String, Object>) custom.get(beforeMapKey);
        } catch (Exception e) {
            return;
        }

        String secretMapKey = configuration.getSecretKey();
        if (!jwtMap.containsKey(secretMapKey)) return;
        Object secret = jwtMap.get(secretMapKey);
        if (secret == null || !(secret instanceof String) || secret.toString().isBlank()) return;

        if (callback.getToken() == null || callback.getToken().isBlank()) {
            String tokenMapKey = configuration.getTokenKey();
            if (!jwtMap.containsKey(tokenMapKey)) return;

            Object token = jwtMap.get(tokenMapKey);
            if (token == null || !(token instanceof String) || token.toString().isBlank()) return;

            callback.setToken(token.toString());
        }

        this.jwtManager.verify(callback, callback.getToken(), secret.toString());
    }
}
