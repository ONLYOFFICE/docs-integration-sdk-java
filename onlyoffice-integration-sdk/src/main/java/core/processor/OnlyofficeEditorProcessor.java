package core.processor;

import core.model.config.Config;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeModelValidator;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class OnlyofficeEditorProcessor implements OnlyofficeProcessor<Config> {
    private final OnlyofficeConfig configUtil;

    @Inject
    public OnlyofficeEditorProcessor(OnlyofficeConfig configUtil) {
        this.configUtil = configUtil;
    }

    /**
     *
     * @param config
     * @return
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void process(Config config) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (config == null)
            throw new OnlyofficeProcessRuntimeException("Config or config request is null");

        this.configUtil.sanitizeConfig(config);
        OnlyofficeModelValidator.validate(config);
    }
}
