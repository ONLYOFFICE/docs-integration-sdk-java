package core.util;

import core.model.config.Config;
import exception.OnlyofficeInvalidParameterRuntimeException;

public interface OnlyofficeConfig {
    /**
     *
     * @param config
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    void sanitizeConfig(Config config) throws OnlyofficeInvalidParameterRuntimeException;
}
