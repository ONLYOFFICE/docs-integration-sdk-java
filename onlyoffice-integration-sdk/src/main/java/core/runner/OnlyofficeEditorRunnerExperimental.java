package core.runner;

import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.config.Config;
import exception.*;

public class OnlyofficeEditorRunnerExperimental implements OnlyofficeEditorRunner {
    public Config run(Config config) throws OnlyofficeProcessBeforeException, OnlyofficeInvalidParameterException, OnlyofficeProcessAfterException, OnlyofficeEditorRunnerException, JWTVerificationException, OnlyofficeJwtSigningException {
        return null;
    }
}
