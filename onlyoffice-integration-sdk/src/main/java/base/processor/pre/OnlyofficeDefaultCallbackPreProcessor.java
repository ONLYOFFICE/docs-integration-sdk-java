package base.processor.pre;

import com.google.common.collect.ImmutableMap;
import core.model.OnlyofficeModelMutator;
import core.model.callback.Callback;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.runner.callback.CallbackRequest;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OnlyofficeDefaultCallbackPreProcessor implements OnlyofficeCallbackPreProcessor {
    private final OnlyofficeJwtSecurity jwtManager = new OnlyofficeJwtSecurityManager();

    /**
     *
     * @param request
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void processBefore(CallbackRequest request) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (request == null || request.getCallback() == null) return;
        if (!request.hasPreProcessor(preprocessorName())) return;

        ImmutableMap<String, Object> processorData = request.getPreProcessorSchema(preprocessorName());
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

            mutator.mutate(request.getCallback());
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