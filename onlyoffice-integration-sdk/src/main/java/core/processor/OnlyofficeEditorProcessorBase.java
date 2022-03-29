package core.processor;

import core.model.config.Config;
import core.util.OnlyofficeConfigUtil;
import core.util.OnlyofficeValidationCaller;
import exception.OnlyofficeEditorConfigurationException;
import exception.OnlyofficeInvalidParameterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OnlyofficeEditorProcessorBase implements OnlyofficeEditorProcessor {
    private final OnlyofficeConfigUtil configUtil;

    public final Config processEditorConfig(Config config) throws OnlyofficeEditorConfigurationException, OnlyofficeInvalidParameterException {
        OnlyofficeValidationCaller.validate(config);
        return this.configUtil.sanitizeConfig(config);
    }
}
