package core.processor;

import com.auth0.jwt.exceptions.JWTCreationException;
import core.model.config.Config;
import core.security.OnlyofficeJwtManager;
import core.util.OnlyofficeConfigUtil;
import core.util.OnlyofficeValidationCaller;
import exception.OnlyofficeEditorConfigurationException;
import exception.OnlyofficeInvalidParameterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RequiredArgsConstructor
@Getter
public class OnlyofficeEditorProcessorBase implements OnlyofficeEditorProcessor {
    private final OnlyofficeConfigUtil configUtil;
    private final OnlyofficeJwtManager jwtManager;
    @Setter
    private int jwtExpirationMinutes = 1;

    /**
     * @param config
     * @return
     * @throws OnlyofficeEditorConfigurationException
     * @throws OnlyofficeInvalidParameterException
     * @throws JWTCreationException
     */
    public final Config processEditorConfig(Config config) throws OnlyofficeEditorConfigurationException, OnlyofficeInvalidParameterException, JWTCreationException {
        if (config == null)
            throw new OnlyofficeEditorConfigurationException("ONLYOFFICE Config object is null");

        String secret = config.getSecret();
        if (secret != null && !secret.isBlank()) {
            LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(this.jwtExpirationMinutes, ChronoUnit.MINUTES));
            Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            String token = this.jwtManager.sign(config, secret, date).get();
            config.setToken(token);
        }

        OnlyofficeValidationCaller.validate(config);
        return this.configUtil.sanitizeConfig(config);
    }
}
