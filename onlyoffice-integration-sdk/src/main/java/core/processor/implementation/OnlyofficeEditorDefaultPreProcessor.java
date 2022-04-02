package core.processor.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import core.processor.OnlyofficePreProcessor;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeProcessBeforeException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeEditorDefaultPreProcessor implements OnlyofficePreProcessor<Config> {
    private final OnlyofficeJwtManager jwtManager;

    /**
     *
     * @param config
     * @throws OnlyofficeProcessBeforeException
     * @throws JWTVerificationException
     */
    public void processBefore(Config config) throws OnlyofficeProcessBeforeException, JWTVerificationException {
        if (config == null) return;

        Map<String, Map<String, Object>> processors = config.getProcessors();
        if (!processors.containsKey("onlyoffice.default.preprocessor.editor")) return;

        Map<String, Object> processorData = processors.get("onlyoffice.default.preprocessor.editor");
        if (processorData == null) return;

        Object key = processorData.get("key");
        Object token = processorData.get("token");
        if (key == null) return;
        if (token == null) return;

        try {
            if (processorData.get("mutator") == null)
                throw new ClassCastException("Config JWT mutator was not found");

            OnlyofficeModelMutator<Config> mutator = (OnlyofficeModelMutator<Config>) processorData.get("mutator");
            this.jwtManager.verify(mutator, token.toString(), key.toString());
            mutator.mutate(config);
        } catch (ClassCastException e) {
            this.jwtManager.verify(token.toString(), key.toString());
        }
    }
}
