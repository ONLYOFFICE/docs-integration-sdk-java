package core.util;

import core.model.config.Config;
import exception.OnlyofficeEditorConfigurationException;

public interface OnlyofficeConfigUtil {
    Config sanitizeConfig(Config config) throws OnlyofficeEditorConfigurationException;
}
