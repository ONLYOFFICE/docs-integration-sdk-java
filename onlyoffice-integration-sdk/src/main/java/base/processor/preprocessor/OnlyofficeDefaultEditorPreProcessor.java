package base.processor.preprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import core.processor.preprocessor.OnlyofficeEditorPreProcessor;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;

import java.util.Map;

public class OnlyofficeDefaultEditorPreProcessor extends OnlyofficeEditorPreProcessor<DefaultEditorSchema> {
    private final ObjectMapper objectMapper;
    private final OnlyofficeJwtSecurity jwtManager;

    public OnlyofficeDefaultEditorPreProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jwtManager = new OnlyofficeJwtSecurityManager(this.objectMapper);
    }

    /**
     *
     * @param customData
     * @param schema
     * @return
     */
    public DefaultEditorSchema validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema) {
        if (schema == null) return null;

        Object key = schema.get("key");
        Object token = schema.get("token");

        if (key == null || token == null) return null;

        if (schema.get("mutator") == null)
            return new DefaultEditorSchema(key.toString(), token.toString());

        try {
            OnlyofficeModelMutator<Config> mutator = (OnlyofficeModelMutator<Config>) schema.get("mutator");
            return new DefaultEditorSchema(key.toString(), token.toString(), mutator);
        } catch (ClassCastException e) {
            return new DefaultEditorSchema(key.toString(), token.toString());
        }
    }

    /**
     *
     * @param config
     * @param validSchema
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void processBefore(Config config, DefaultEditorSchema validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        try {
            if (validSchema.getMutator() == null) {
                this.jwtManager.verify(validSchema.getToken(), validSchema.getKey());
                return;
            }
            this.jwtManager.verify(validSchema.getMutator(), validSchema.getToken(), validSchema.getKey());
            validSchema.getMutator().mutate(config);
        } catch (OnlyofficeJwtVerificationRuntimeException e) {
            throw new OnlyofficeProcessBeforeRuntimeException(e.getMessage(), e);
        }
    }

    /**
     *
     * @return
     */
    public String preprocessorName() {
        return "onlyoffice.preprocessor.default.editor";
    }
}
