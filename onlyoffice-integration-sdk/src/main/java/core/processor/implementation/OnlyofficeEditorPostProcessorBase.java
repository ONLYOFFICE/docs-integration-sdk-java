package core.processor.implementation;

import core.model.config.Config;
import core.processor.OnlyofficePostProcessor;
import core.processor.configuration.OnlyofficeDefaultProcessorCustomMapConfiguration;
import core.security.OnlyofficeJwtManager;
import exception.OnlyofficeJwtSigningException;
import exception.OnlyofficeProcessAfterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeEditorPostProcessorBase implements OnlyofficePostProcessor<Config> {
    private final OnlyofficeDefaultProcessorCustomMapConfiguration configuration;
    private final OnlyofficeJwtManager jwtManager;
    @Getter
    @Setter
    private int jwtExpirationMinutes = 2;

    public void processAfter(Config config) throws OnlyofficeJwtSigningException {
        if (config == null) return;

        String afterMapKey = configuration.getAfterMapKey();
        Map<String, Object> custom = config.getCustom();
        if (!custom.containsKey(afterMapKey)) return;

        if (!(custom.get(afterMapKey) instanceof Map)) return;
        Map<String, Object> jwtMap;
        try {
            jwtMap = (Map<String, Object>) custom.get(afterMapKey);
        } catch (Exception e) {
            return;
        }

        String secretMapKey = configuration.getSecretKey();
        if (!jwtMap.containsKey(secretMapKey)) return;
        Object secret = jwtMap.get(secretMapKey);
        if (secret == null || !(secret instanceof String) || secret.toString().isBlank()) return;

        LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(this.jwtExpirationMinutes, ChronoUnit.MINUTES));
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        String token = this.jwtManager.sign(config, secret.toString(), date)
                .orElseThrow(() -> new OnlyofficeProcessAfterException("Could not sign an instance of ONLYOFFICE Config"));
        config.setToken(token);
    }
}
