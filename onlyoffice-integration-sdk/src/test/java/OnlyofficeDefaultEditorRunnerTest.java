import base.processor.OnlyofficeDefaultEditorProcessor;
import base.processor.pre.OnlyofficeDefaultEditorPreProcessor;
import base.runner.editor.OnlyofficeDefaultEditorRunner;
import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.security.OnlyofficeJwtSecurityManager;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeFile;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Replace with mocks
public class OnlyofficeDefaultEditorRunnerTest {
    private final OnlyofficeJwtSecurityManager jwtSecurity = new OnlyofficeJwtSecurityManager();
    private final OnlyofficeFile onlyofficeFile = new OnlyofficeFileUtil();
    private final OnlyofficeConfig configUtil = new OnlyofficeConfigUtil(onlyofficeFile);
    private final OnlyofficeDefaultEditorProcessor onlyofficeDefaultEditorProcessor = new OnlyofficeDefaultEditorProcessor(configUtil, jwtSecurity);
    private final OnlyofficeEditorPreProcessor configOnlyofficePreProcessor = new OnlyofficeDefaultEditorPreProcessor(jwtSecurity);
    private final OnlyofficeDefaultEditorRunner onlyofficeDefaultEditorRunner = new OnlyofficeDefaultEditorRunner(
            onlyofficeDefaultEditorProcessor,
            List.of(configOnlyofficePreProcessor),
            List.of()
    );

    @Test
    @SneakyThrows
    public void runFullValidTest() {
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
                .processors(Map.of(
                        "onlyoffice.preprocessor.default.editor", Map.of(
                                "key", "secret",
                                "token", token,
                                "mutator", af
                        )
                ))
                .build();
        this.onlyofficeDefaultEditorRunner.run(config);
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
        this.onlyofficeDefaultEditorRunner.run(config);
        assertNotNull(config);
        assertNull(config.getToken());
    }
}
