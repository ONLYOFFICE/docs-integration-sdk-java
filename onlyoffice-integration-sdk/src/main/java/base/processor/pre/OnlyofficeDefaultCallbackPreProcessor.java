package base.processor.pre;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class OnlyofficeDefaultCallbackPreProcessor implements OnlyofficeCallbackPreProcessor {
    private final ObjectMapper objectMapper;
    private final OnlyofficeJwtSecurity jwtManager;

    public OnlyofficeDefaultCallbackPreProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jwtManager = new OnlyofficeJwtSecurityManager(this.objectMapper);
    }

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
        if (key == null || token == null) return;

        if (processorData.get("mutator") == null) {
            try {
                this.jwtManager.verify(token.toString(), key.toString());
                return;
            } catch (OnlyofficeJwtVerificationRuntimeException e) {
                throw new OnlyofficeProcessBeforeRuntimeException(e.getMessage());
            }
        }

        try {
            OnlyofficeModelMutator<Callback> mutator = (OnlyofficeModelMutator<Callback>) processorData.get("mutator");
            try {
                this.jwtManager.verify(mutator, token.toString(), key.toString());
            } catch (OnlyofficeJwtVerificationRuntimeException e) {
                throw new OnlyofficeProcessBeforeRuntimeException(e.getMessage());
            }
            mutator.mutate(request.getCallback());
        } catch (ClassCastException e) {
            throw new OnlyofficeProcessBeforeRuntimeException("Expected to find a Callback mutator under schema's 'mutator' key. Got unknown");
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