package core.util;

import core.model.config.Config;
import exception.OnlyofficeEditorConfigurationException;

public interface OnlyofficeConfigUtil {
    /**
     *
     * @param config
     * @return
     * @throws OnlyofficeEditorConfigurationException
     */
    Config sanitizeConfig(Config config) throws OnlyofficeEditorConfigurationException;
}
