package integration;

import base.processor.postprocessor.OnlyofficeEditorJwtPostProcessor;
import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficeEditorPostProcessor;
import core.processor.OnlyofficeEditorProcessor;
import core.runner.OnlyofficeEditorRunner;
import core.runner.implementation.OnlyofficeSequentialEditorRunner;
import core.security.OnlyofficeJwtSecurityManager;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeFile;
import exception.OnlyofficeRunnerRuntimeException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeSequentialEditorRunnerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeJwtSecurityManager jwtSecurity = new OnlyofficeJwtSecurityManager(objectMapper);
    private final OnlyofficeFile onlyofficeFile = new OnlyofficeFileUtil();
    private final OnlyofficeConfig configUtil = new OnlyofficeConfigUtil(onlyofficeFile);
    private final OnlyofficeEditorProcessor onlyofficeDefaultEditorProcessor = new OnlyofficeEditorProcessor(configUtil);
    private OnlyofficeEditorPostProcessor configOnlyofficePostProcessor = new OnlyofficeEditorJwtPostProcessor(jwtSecurity);
    private final OnlyofficeEditorRunner onlyofficeDefaultEditorRunner = new OnlyofficeSequentialEditorRunner(
            onlyofficeDefaultEditorProcessor,
            Set.of(),
            Set.of(configOnlyofficePostProcessor)
    );

    @Test
    public void runNullRequestTest() {
        configOnlyofficePostProcessor.setJwtSecret("secret");
        assertThrows(OnlyofficeRunnerRuntimeException.class, () -> this.onlyofficeDefaultEditorRunner.run(null));
    }


    @Test
    @SneakyThrows
    public void runFullValidRequestTest() {
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
        configOnlyofficePostProcessor.setJwtSecret("secret");
        this.onlyofficeDefaultEditorRunner.run(config);
        assertNotNull(config);
        assertNotNull(config.getToken());
        assertEquals("https://example.com", config.getDocument().getUrl());
    }
}
