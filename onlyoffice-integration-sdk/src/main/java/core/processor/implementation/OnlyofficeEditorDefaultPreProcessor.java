package core.processor.implementation;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.OnlyofficeModelAutoFiller;
import core.model.config.Config;
import core.processor.OnlyofficePreProcessor;
import core.processor.configuration.OnlyofficeDefaultPrePostProcessorCustomMapConfiguration;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeProcessBeforeException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeEditorDefaultPreProcessor implements OnlyofficePreProcessor<Config> {
    private final OnlyofficeDefaultPrePostProcessorCustomMapConfiguration configuration;
    private final OnlyofficeJwtManager jwtManager;

    /**
     *
     * @param config
     * @throws OnlyofficeProcessBeforeException
     * @throws JWTVerificationException
     */
    public void processBefore(Config config) throws OnlyofficeProcessBeforeException, JWTVerificationException {
        if (config == null) return;
        Map<String, Object> custom = config.getCustom();

        String beforeMapKey = this.configuration.getBeforeMapKey();
        if (!custom.containsKey(beforeMapKey)) return;
        if (!(custom.get(beforeMapKey) instanceof Map)) return;

        Map<String, Object> jwtMap;
        try {
            jwtMap = (Map<String, Object>) custom.get(beforeMapKey);
        } catch (ClassCastException e) {
            return;
        }

        String secretMapKey = configuration.getSecretKey();
        if (!jwtMap.containsKey(secretMapKey)) return;
        Object secret = jwtMap.get(secretMapKey);
        if (secret == null || !(secret instanceof String) || secret.toString().isBlank()) return;

        String tokenMapKey = configuration.getTokenKey();
        if (!jwtMap.containsKey(tokenMapKey)) return;
        Object token = jwtMap.get(tokenMapKey);
        if (token == null || !(token instanceof String) || token.toString().isBlank()) return;

        try {
            this.jwtManager.decode(token.toString());
        } catch (JWTDecodeException e) {
            return;
        }

        String autoFillerMapKey = configuration.getAutoFillerKey();
        if (!jwtMap.containsKey(autoFillerMapKey)) {
            this.jwtManager.verify(token.toString(), secret.toString());
            return;
        }
        Object autoFiller = jwtMap.get(autoFillerMapKey);
        if (autoFiller instanceof OnlyofficeModelAutoFiller) {
            OnlyofficeModelAutoFiller<Config> filler;
            try {
                filler = (OnlyofficeModelAutoFiller<Config>) autoFiller;
            } catch (ClassCastException e) {
                this.jwtManager.verify(token.toString(), secret.toString());
                return;
            }
            this.jwtManager.verify(filler, token.toString(), jwtMap.get(secretMapKey).toString());
            filler.fillModel(config);
        }
    }
}
