package base.processor.pre;

import core.model.OnlyofficeModelMutator;
import core.model.callback.Callback;
import core.processor.OnlyofficePreProcessor;
import core.security.OnlyofficeJwtSecurity;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeDefaultCallbackPreProcessor implements OnlyofficePreProcessor<Callback> {
    private final OnlyofficeJwtSecurity jwtManager;

    /**
     *
     * @param callback
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void processBefore(Callback callback) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (callback == null) return;

        Map<String, Map<String, Object>> processors = callback.getProcessors();
        if (!processors.containsKey(preprocessorName())) return;

        Map<String, Object> processorData = processors.get(preprocessorName());
        if (processorData == null) return;

        Object key = processorData.get("key");
        Object token = processorData.get("token");
        if (key == null) return;
        if (token == null) return;

        try {
            if (processorData.get("mutator") == null)
                throw new ClassCastException("Config JWT mutator was not found");

            OnlyofficeModelMutator<Callback> mutator = (OnlyofficeModelMutator<Callback>) processorData.get("mutator");

            try {
                this.jwtManager.verify(mutator, token.toString(), key.toString());
            } catch (OnlyofficeJwtVerificationRuntimeException e) {
                throw new OnlyofficeProcessBeforeRuntimeException(e.getMessage());
            }

            mutator.mutate(callback);
        } catch (ClassCastException e) {
            try {
                this.jwtManager.verify(token.toString(), key.toString());
            } catch (OnlyofficeJwtVerificationRuntimeException ex) {
                throw new OnlyofficeProcessBeforeRuntimeException(ex.getMessage());
            }
        }
    }

    /**
     *
     * @return
     */
    public String preprocessorName() {
        return "onlyoffice.preprocessor.default.callback";
    }
}