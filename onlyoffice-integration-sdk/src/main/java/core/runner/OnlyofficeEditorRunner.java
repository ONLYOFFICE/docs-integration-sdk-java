package core.runner;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.config.Config;
import exception.*;

public interface OnlyofficeEditorRunner {
    /**
     *
     * @param config
     * @return
     * @throws OnlyofficeProcessBeforeException
     * @throws OnlyofficeInvalidParameterException
     * @throws OnlyofficeProcessAfterException
     * @throws OnlyofficeEditorRunnerException
     * @throws JWTVerificationException
     * @throws OnlyofficeJwtSigningException
     */
    Config run(Config config) throws OnlyofficeProcessBeforeException, OnlyofficeInvalidParameterException, OnlyofficeProcessAfterException, OnlyofficeEditorRunnerException, JWTVerificationException, OnlyofficeJwtSigningException;
}
