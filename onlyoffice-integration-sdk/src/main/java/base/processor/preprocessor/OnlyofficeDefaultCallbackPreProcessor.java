package base.processor.preprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import core.model.OnlyofficeModelMutator;
import core.model.callback.Callback;
import core.processor.preprocessor.OnlyofficeCallbackPreProcessor;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;

import java.util.Map;

public class OnlyofficeDefaultCallbackPreProcessor extends OnlyofficeCallbackPreProcessor<DefaultCallbackSchema> {
    private final ObjectMapper objectMapper;
    private final OnlyofficeJwtSecurity jwtManager;

    public OnlyofficeDefaultCallbackPreProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jwtManager = new OnlyofficeJwtSecurityManager(this.objectMapper);
    }

    /**
     *
     * @param customData
     * @param schema
     * @return
     */
    public DefaultCallbackSchema validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema) {
        if (schema == null) return null;

        Object key = schema.get("key");
        Object token = schema.get("token");
        if (key == null || token == null) return null;

        if (schema.get("mutator") == null)
            return new DefaultCallbackSchema(key.toString(), token.toString());

        try {
            OnlyofficeModelMutator<Callback> mutator = (OnlyofficeModelMutator<Callback>) schema.get("mutator");
            return new DefaultCallbackSchema(key.toString(), token.toString(), mutator);
        } catch (ClassCastException e) {
            return new DefaultCallbackSchema(key.toString(), token.toString());
        }
    }

    /**
     *
     * @param callback
     * @param validSchema
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void processBefore(Callback callback, DefaultCallbackSchema validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        try {
            if (validSchema.getMutator() == null) {
                this.jwtManager.verify(validSchema.getToken(), validSchema.getKey());
                return;
            }
            this.jwtManager.verify(validSchema.getMutator(), validSchema.getToken(), validSchema.getKey());
            validSchema.getMutator().mutate(callback);
        } catch (OnlyofficeJwtVerificationRuntimeException e) {
            throw new OnlyofficeProcessBeforeRuntimeException(e.getMessage(), e);
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