import base.processor.pre.OnlyofficeDefaultEditorPreProcessor;
import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import core.processor.OnlyofficePreProcessor;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessBeforeRuntimeException;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Replace with mocks
public class OnlyofficeDefaultEditorPreProcessorTest {
    private final OnlyofficeJwtSecurityManager jwtManager = new OnlyofficeJwtSecurityManager();
    private final OnlyofficePreProcessor<Config> configOnlyofficePreProcessor = new OnlyofficeDefaultEditorPreProcessor(jwtManager);

    @Test
    public void processNoArgumentsTest() {
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore());
    }

    @Test
    public void processNullConfigTest() {
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(null));
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
        class AF implements OnlyofficeModelMutator<Config> {
            @Getter
            private String docUrl;

            public AF(String docUrl) {
                this.docUrl = docUrl;
            }

            public void mutate(Config model) {
                model.getDocument().setUrl(docUrl);
            }
        }

        AF af = new AF("http://example.com");
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(af, "secret", date).get();

        Config config = Config
                .builder()
                .processors(Map.of(
                        "onlyoffice.preprocessor.default.editor", Map.of(
                                "key", "secret",
                                "token", token,
                                "mutator", af
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
                .processors(Map.of(
                        "onlyoffice.preprocessor.default.editor", Map.of(
                                "key", "secret",
                                "token", token
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
                .processors(Map.of(
                        "onlyoffice.preprocessor.default.editor", Map.of(
                                "key", "invalid",
                                "token", token
                        )
                ))
                .build();
        assertThrows(OnlyofficeProcessBeforeRuntimeException.class, () -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processIgnoreJwtVerificationNoSecretTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        Config config = Config
                .builder()
                .processors(Map.of(
                        "onlyoffice.preprocessor.default.editor", Map.of(
                                "token", token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }

    @Test
    public void processIgnoreJwtVerificationNoTokenTest() {
        Config config = Config
                .builder()
                .processors(Map.of(
                        "onlyoffice.preprocessor.default.editor", Map.of(
                                "key", "secret"
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(config));
    }
}
