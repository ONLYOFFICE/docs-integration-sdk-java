package base.processor.postprocessor;

import core.model.config.Config;
import core.processor.OnlyofficeEditorPostProcessor;
import core.security.OnlyofficeJwtSecurity;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Singleton
public class OnlyofficeEditorJwtPostProcessor implements OnlyofficeEditorPostProcessor {
    private final OnlyofficeJwtSecurity jwtManager;
    private String jwtSecret;

    @Inject
    public OnlyofficeEditorJwtPostProcessor(OnlyofficeJwtSecurity jwtManager) {
        this.jwtManager = jwtManager;
    }

    /**
     *
     * @param config
     * @return
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void processAfter(Config config) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        try {
            if (jwtSecret != null && !jwtSecret.isBlank()) {
                LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(5, ChronoUnit.MINUTES));
                Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
                config.setToken(this.jwtManager.sign(config, jwtSecret, date).get());
            }
        } catch (Exception e) {
            throw new OnlyofficeProcessAfterRuntimeException(e.getMessage(), e);
        }
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
}
