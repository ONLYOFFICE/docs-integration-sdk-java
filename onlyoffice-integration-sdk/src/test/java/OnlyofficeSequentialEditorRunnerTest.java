import base.processor.OnlyofficeDefaultEditorProcessor;
import base.processor.pre.OnlyofficeDefaultEditorPreProcessor;
import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.runner.OnlyofficeEditorRunner;
import core.runner.editor.ConfigRequest;
import core.runner.editor.OnlyofficeSequentialEditorRunner;
import core.security.OnlyofficeJwtSecurityManager;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeFile;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Replace with mocks
public class OnlyofficeSequentialEditorRunnerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeJwtSecurityManager jwtSecurity = new OnlyofficeJwtSecurityManager(objectMapper);
    private final OnlyofficeFile onlyofficeFile = new OnlyofficeFileUtil();
    private final OnlyofficeConfig configUtil = new OnlyofficeConfigUtil(onlyofficeFile);
    private final OnlyofficeEditorProcessor onlyofficeDefaultEditorProcessor = new OnlyofficeDefaultEditorProcessor(configUtil, jwtSecurity);
    private final OnlyofficeEditorPreProcessor configOnlyofficePreProcessor = new OnlyofficeDefaultEditorPreProcessor(objectMapper);
    private final OnlyofficeEditorRunner onlyofficeDefaultEditorRunner = new OnlyofficeSequentialEditorRunner(
            onlyofficeDefaultEditorProcessor,
            Map.of(configOnlyofficePreProcessor.preprocessorName(), configOnlyofficePreProcessor),
            Map.of()
    );

    @Test
    public void runNullRequestTest() {
        assertThrows(OnlyofficeRunnerRuntimeException.class, () -> this.onlyofficeDefaultEditorRunner.run(null));
    }

    @Test
    public void runNullConfigTest() {
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.onlyofficeDefaultEditorRunner.run(
                ConfigRequest
                        .builder()
                        .config(null)
                        .build()
        ));
    }

    @Test
    @SneakyThrows
    public void runFullValidRequestTest() {
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
        String token = this.jwtSecurity.sign(af, "secret", date).get();

        Document doc = Document
                .builder()
                .title("test.docx")
                .url("https://example.co")
                .key("asdd")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(doc)
                .editorConfig(editor)
                .secret("secret")
                .build();
        this.onlyofficeDefaultEditorRunner.run(
                ConfigRequest
                        .builder()
                        .config(config)
                        .build()
                        .addPreProcessor("onlyoffice.preprocessor.default.editor", ImmutableMap.of(
                                "key", "secret",
                                "token", token,
                                "mutator", af
                        ))
        );
        assertNotNull(config);
        assertNotNull(config.getToken());
        assertEquals("http://example.com", config.getDocument().getUrl());
    }

    @Test
    @SneakyThrows
    public void runNoSecretValidTest() {
        Document doc = Document
                .builder()
                .title("test.docx")
                .url("https://example.com")
                .key("asdd")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(doc)
                .editorConfig(editor)
                .build();
        this.onlyofficeDefaultEditorRunner.run(
                ConfigRequest
                        .builder()
                        .config(config)
                        .build()
        );
        assertNotNull(config);
        assertNull(config.getToken());
    }
}
