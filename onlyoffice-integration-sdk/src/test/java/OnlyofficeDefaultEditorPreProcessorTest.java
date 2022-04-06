import base.processor.pre.OnlyofficeDefaultEditorPreProcessor;
import com.google.common.collect.ImmutableMap;
import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.runner.editor.ConfigRequest;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessBeforeRuntimeException;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeDefaultEditorPreProcessorTest {
    private final OnlyofficeJwtSecurityManager jwtManager = new OnlyofficeJwtSecurityManager();
    private final OnlyofficeEditorPreProcessor configOnlyofficePreProcessor = new OnlyofficeDefaultEditorPreProcessor();

    @Test
    public void processNoArgumentsTest() {
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore());
    }

    @Test
    public void processNullConfigRequestTest() {
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(null));
    }

    @Test
    public void processNullConfigTest() {
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(
                ConfigRequest.builder().build()
        ));
    }

    @Test
    public void processNoJwtVerificationConfigTest() {
        Config config = Config
                .builder()
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(
                ConfigRequest
                        .builder()
                        .config(config)
                        .build()
        ));
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
                .build();

        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(
                ConfigRequest
                        .builder()
                        .config(config)
                        .preProcessors(new LinkedHashMap<>(){{
                            put("onlyoffice.preprocessor.default.editor", ImmutableMap.of(
                                    "key", "secret",
                                    "token", token,
                                    "mutator", af
                            ));
                        }})
                        .build()
        ));
        assertEquals("http://example.com", config.getDocument().getUrl());
    }

    @Test
    public void processValidJwtTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(
                ConfigRequest
                        .builder()
                        .preProcessors(new LinkedHashMap<>())
                        .build()
        ));
    }

    @Test
    public void processInvalidJwtSecretTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        assertThrows(OnlyofficeProcessBeforeRuntimeException.class, () -> this.configOnlyofficePreProcessor.processBefore(
                ConfigRequest
                        .builder()
                        .config(Config.builder().build())
                        .preProcessors(new LinkedHashMap<>(){{
                            put("onlyoffice.preprocessor.default.editor", ImmutableMap.of(
                                    "key", "invalid",
                                    "token", token
                            ));
                        }})
                        .build()
        ));
    }

    @Test
    public void processIgnoreJwtVerificationNoSecretTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("test", "test"), "secret", date).get();
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(
                ConfigRequest
                        .builder()
                        .config(Config.builder().build())
                        .preProcessors(new LinkedHashMap<>(){{
                            put("onlyoffice.preprocessor.default.editor", ImmutableMap.of(
                                    "token", token
                            ));
                        }})
                        .build()
        ));
    }

    @Test
    public void processIgnoreJwtVerificationNoTokenTest() {
        assertDoesNotThrow(() -> this.configOnlyofficePreProcessor.processBefore(
                ConfigRequest
                        .builder()
                        .config(Config.builder().build())
                        .preProcessors(new LinkedHashMap<>(){{
                            put("onlyoffice.preprocessor.default.editor", ImmutableMap.of(
                                    "key", "secret"
                            ));
                        }})
                        .build()
        ));
    }
}
