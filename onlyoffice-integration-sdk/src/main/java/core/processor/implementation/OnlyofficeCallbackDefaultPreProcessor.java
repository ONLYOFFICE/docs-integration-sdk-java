package core.processor.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.OnlyofficeModelMutator;
import core.model.callback.Callback;
import core.processor.OnlyofficePreProcessor;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeProcessBeforeException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeCallbackDefaultPreProcessor implements OnlyofficePreProcessor<Callback> {
    private final OnlyofficeJwtManager jwtManager;

    /**
     *
     * @param callback
     * @throws OnlyofficeProcessBeforeException
     * @throws JWTVerificationException
     */
    public void processBefore(Callback callback) throws OnlyofficeProcessBeforeException, JWTVerificationException {
        if (callback == null) return;

        Map<String, Map<String, Object>> processors = callback.getProcessors();
        if (!processors.containsKey("onlyoffice.default.preprocessor.callback")) return;

        Map<String, Object> processorData = processors.get("onlyoffice.default.preprocessor.callback");
        if (processorData == null) return;

        Object key = processorData.get("key");
        Object token = processorData.get("token");
        if (key == null) return;
        if (token == null) return;

        try {
            if (processorData.get("mutator") == null)
                throw new ClassCastException("Config JWT mutator was not found");

            OnlyofficeModelMutator<Callback> mutator = (OnlyofficeModelMutator<Callback>) processorData.get("mutator");
            this.jwtManager.verify(mutator, token.toString(), key.toString());
            mutator.mutate(callback);
        } catch (ClassCastException e) {
            this.jwtManager.verify(token.toString(), key.toString());
        }
    }
}