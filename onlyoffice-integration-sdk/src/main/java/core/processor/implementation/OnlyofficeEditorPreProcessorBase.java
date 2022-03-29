package core.processor.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.OnlyofficeModelAutoFiller;
import core.model.config.Config;
import core.processor.OnlyofficePreProcessor;
import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeProcessBeforeException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeEditorPreProcessorBase implements OnlyofficePreProcessor<Config> {
    private final OnlyofficeProcessorCustomMapConfiguration configuration;
    private final OnlyofficeJwtManager jwtManager;

    public void processBefore(Config config) throws OnlyofficeProcessBeforeException, JWTVerificationException {
        if (config == null) return;

        String beforeMapKey = this.configuration.getBeforeMapKey();

        Map<String, Object> custom = config.getCustom();
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

        String tokenMapKey = configuration.getTokenKey();
        if (!jwtMap.containsKey(tokenMapKey)) return;

        Object token = jwtMap.get(tokenMapKey);
        if (token == null || !(token instanceof String) || token.toString().isBlank()) return;

        String autoFillerMapKey = configuration.getAutoFillerKey();
        if (jwtMap.containsKey(autoFillerMapKey)) {
            Object autoFiller = jwtMap.get(autoFillerMapKey);
            if (autoFiller instanceof OnlyofficeModelAutoFiller) {
                OnlyofficeModelAutoFiller<Config> filler;
                try {
                    filler = (OnlyofficeModelAutoFiller<Config>) autoFiller;
                    this.jwtManager.verify(filler, token.toString(), jwtMap.get(secretMapKey).toString());
                    filler.fillModel(config);
                    return;
                } catch (Exception e) {
                    this.jwtManager.verify(token.toString(), secret.toString());
                    return;
                }
            }
        }

        this.jwtManager.verify(token.toString(), secret.toString());
    }
}
