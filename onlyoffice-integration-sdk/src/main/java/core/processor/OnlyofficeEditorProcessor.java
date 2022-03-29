package core.processor;

import core.model.config.Config;
import exception.OnlyofficeEditorConfigurationException;
import exception.OnlyofficeInvalidParameterException;

public interface OnlyofficeEditorProcessor {
    /**
     *
     * @param parameters
     * @return
     * @throws OnlyofficeEditorConfigurationException
     * @throws OnlyofficeInvalidParameterException
     */
    Config processEditorConfig(Config parameters) throws OnlyofficeEditorConfigurationException, OnlyofficeInvalidParameterException;
}
