package core.runner;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.config.Config;
import exception.OnlyofficeEditorRunnerException;
import exception.OnlyofficeJwtSigningException;
import exception.OnlyofficeProcessAfterException;
import exception.OnlyofficeProcessBeforeException;

public interface OnlyofficeEditorRunner {
    Config run(Config config) throws OnlyofficeProcessBeforeException, OnlyofficeProcessAfterException, OnlyofficeEditorRunnerException, JWTVerificationException, OnlyofficeJwtSigningException;
}
