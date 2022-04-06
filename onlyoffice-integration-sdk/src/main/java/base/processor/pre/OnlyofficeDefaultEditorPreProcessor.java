package base.processor.pre;

import com.google.common.collect.ImmutableMap;
import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.runner.editor.ConfigRequest;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OnlyofficeDefaultEditorPreProcessor implements OnlyofficeEditorPreProcessor {
    private final OnlyofficeJwtSecurity jwtManager = new OnlyofficeJwtSecurityManager();

    /**
     *
     * @param request
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void processBefore(ConfigRequest request) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (request == null || request.getConfig() == null) return;
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

            OnlyofficeModelMutator<Config> mutator = (OnlyofficeModelMutator<Config>) processorData.get("mutator");
            try {
                this.jwtManager.verify(mutator, token.toString(), key.toString());
            } catch (OnlyofficeJwtVerificationRuntimeException e) {
                throw new OnlyofficeProcessBeforeRuntimeException(e.getMessage());
            }

            mutator.mutate(request.getConfig());
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
        return "onlyoffice.preprocessor.default.editor";
    }
}
