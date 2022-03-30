package core.processor.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.OnlyofficeModelAutoFiller;
import core.model.callback.Callback;
import core.processor.OnlyofficePreProcessor;
import core.processor.configuration.OnlyofficeDefaultPrePostProcessorCustomMapConfiguration;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeProcessBeforeException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeCallbackDefaultPreProcessor implements OnlyofficePreProcessor<Callback> {
    private final OnlyofficeDefaultPrePostProcessorCustomMapConfiguration configuration;
    private final OnlyofficeJwtManager jwtManager;

    /**
     *
     * @param callback
     * @throws OnlyofficeProcessBeforeException
     * @throws JWTVerificationException
     */
    public void processBefore(Callback callback) throws OnlyofficeProcessBeforeException, JWTVerificationException {
        if (callback == null) return;
        Map<String, ?> custom = callback.getCustom();

        String beforeMapKey = this.configuration.getBeforeMapKey();
        if (!custom.containsKey(beforeMapKey)) return;

        Map<String, Object> jwtMap;
        try {
            jwtMap = (Map<String, Object>) custom.get(beforeMapKey);
        } catch (ClassCastException e) {
            return;
        }

        String secretMapKey = configuration.getSecretKey();
        if (!jwtMap.containsKey(secretMapKey)) return;
        Object secret = jwtMap.get(secretMapKey);
        if (secret == null || secret.toString().isBlank()) return;

        String tokenMapKey = configuration.getTokenKey();
        if (!jwtMap.containsKey(tokenMapKey)) return;
        Object token = jwtMap.get(tokenMapKey);
        if (token == null || token.toString().isBlank()) return;

        String autoFillerMapKey = configuration.getAutoFillerKey();
        if (!jwtMap.containsKey(autoFillerMapKey)) {
            this.jwtManager.verify(token.toString(), jwtMap.get(secretMapKey).toString());
            return;
        }

        OnlyofficeModelAutoFiller<Callback> filler;
        try {
            filler = (OnlyofficeModelAutoFiller<Callback>) jwtMap.get(autoFillerMapKey);
        } catch (ClassCastException e) {
            this.jwtManager.verify(token.toString(), jwtMap.get(secretMapKey).toString());
            return;
        }

        this.jwtManager.verify(filler, token.toString(), jwtMap.get(secretMapKey).toString());
        filler.fillModel(callback);
    }
}