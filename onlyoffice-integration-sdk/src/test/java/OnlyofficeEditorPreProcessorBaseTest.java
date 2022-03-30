import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.OnlyofficeModelAutoFiller;
import core.model.config.Config;
import core.processor.OnlyofficePreProcessor;
import core.processor.configuration.OnlyofficeDefaultProcessorCustomMapConfiguration;
import core.processor.implementation.OnlyofficeEditorPreProcessorBase;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeEditorPreProcessorBaseTest {
    private final OnlyofficeDefaultProcessorCustomMapConfiguration configuration = new OnlyofficeDefaultProcessorCustomMapConfiguration();
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final OnlyofficePreProcessor<Config> configOnlyofficePreProcessor = new OnlyofficeEditorPreProcessorBase(configuration, jwtManager);

    @Test
    public void processNoArgumentsTest() {
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore());
    }

    @Test
    public void processNoJwtVerificationConfigTest() {
        Config config = Config
                .builder()
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
        assertNotNull(config.getCustom());
    }

    @Test
    public void processConvertJwtAutoFillerTest() {
        class AF implements OnlyofficeModelAutoFiller<Config> {
            @Getter
            private String docUrl;

            public AF(String docUrl) {
                this.docUrl = docUrl;
            }

            public void fillModel(Config model) {
                model.getDocument().setUrl(docUrl);
            }
        }

        AF af = new AF("http://example.com");
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(af, "secret", date).get();

        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                configuration.getTokenKey(), token,
                                configuration.getAutoFillerKey(), af
                        )
                ))
                .build();

        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
        assertEquals("http://example.com", config.getDocument().getUrl());
    }

    @Test
    public void processValidJwtTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processInvalidJwtSecretTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "invalid",
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertThrows(JWTVerificationException.class, () -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processIgnoreJwtVerificationNoSecretTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processIgnoreJwtVerificationNoTokenTest() {
        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret"
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processIgnoreJwtVerificationInvalidSecretKeyTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                "invalid", "secret",
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processIgnoreJwtVerificationInvalidTokenKeyTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                "invalid", token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processIgnoreJwtVerificationMalformedJwtTest() {
        String token = "Asdasdascascasc.asgqwgqw.qweqwrqwr";
        Config config = Config
                .builder()
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                "invalid", token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }
}
