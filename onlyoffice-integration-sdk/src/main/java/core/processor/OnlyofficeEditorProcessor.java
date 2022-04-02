package core.processor;

import com.auth0.jwt.exceptions.JWTCreationException;
import core.model.config.Config;
import exception.OnlyofficeEditorConfigurationException;
import exception.OnlyofficeInvalidParameterException;

public interface OnlyofficeEditorProcessor {
    /**
     *
     * @param config
     * @return
     * @throws OnlyofficeEditorConfigurationException
     * @throws OnlyofficeInvalidParameterException
     * @throws JWTCreationException
     */
    Config processEditorConfig(Config config) throws OnlyofficeEditorConfigurationException,  OnlyofficeInvalidParameterException, JWTCreationException;
}
