package base.processor;

import core.model.config.Config;
import core.processor.OnlyofficeEditorProcessor;
import core.security.OnlyofficeJwtSecurity;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeModelValidator;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeJwtSigningRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
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
public class OnlyofficeDefaultEditorProcessor implements OnlyofficeEditorProcessor {
    private final OnlyofficeConfig configUtil;
    private final OnlyofficeJwtSecurity jwtManager;
    @Setter
    private int jwtExpirationMinutes = 1;

    /**
     *
     * @param config
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void process(Config config) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {
        if (config == null)
            throw new OnlyofficeProcessRuntimeException("Config or config request is null");

        String secret = config.getSecret();
        if (secret != null && !secret.isBlank()) {
            LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(this.jwtExpirationMinutes, ChronoUnit.MINUTES));
            Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            try {
                String token = this.jwtManager.sign(config, secret, date).get();
                config.setToken(token);
            } catch (OnlyofficeJwtSigningRuntimeException e) {
                throw new OnlyofficeProcessRuntimeException(e.getMessage());
            }
        }

        this.configUtil.sanitizeConfig(config);
        OnlyofficeModelValidator.validate(config);
    }
}
