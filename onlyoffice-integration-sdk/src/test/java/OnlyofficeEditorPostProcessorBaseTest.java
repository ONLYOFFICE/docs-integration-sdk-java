import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficePostProcessor;
import core.processor.configuration.OnlyofficeDefaultProcessorCustomMapConfiguration;
import core.processor.implementation.OnlyofficeEditorPostProcessorBase;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeEditorPostProcessorBaseTest {
    private final OnlyofficeDefaultProcessorCustomMapConfiguration configuration = new OnlyofficeDefaultProcessorCustomMapConfiguration();
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final OnlyofficePostProcessor<Config> configOnlyofficePostProcessor = new OnlyofficeEditorPostProcessorBase(configuration, jwtManager);

    @Test
    public void processNoArguments() {
        assertDoesNotThrow(() -> this.configOnlyofficePostProcessor.processAfter());
    }

    @Test
    public void processNoSecretSignatureTest() {
        Document document = Document
                .builder()
                .title("test.docx")
                .key("asdfd")
                .url("https://example.com")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .build();

        assertDoesNotThrow(() -> this.configOnlyofficePostProcessor.processAfter(config));
        assertNull(config.getToken());
        assertNotNull(config.getCustom());
    }

    @Test
    public void processInvalidSecretKeySignatureTest() {
        Document document = Document
                .builder()
                .title("test.docx")
                .key("asdfd")
                .url("https://example.com")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .custom(Map.of(
                        configuration.getAfterMapKey(), Map.of(
                                "invalid", "secret"
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePostProcessor.processAfter(config));
        assertNull(config.getToken());
    }

    @Test
    public void processInvalidMainMapSignatureTest() {
        Document document = Document
                .builder()
                .title("test.docx")
                .key("asdfd")
                .url("https://example.com")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .custom(Map.of(
                        "invalid", Map.of(
                                "invalid", "secret"
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePostProcessor.processAfter(config));
        assertNull(config.getToken());
    }

    @Test
    public void processSecretSignatureTest() {
        Document document = Document
                .builder()
                .title("test.docx")
                .key("asdfd")
                .url("https://example.com")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .custom(Map.of(
                        configuration.getAfterMapKey(), Map.of(
                                configuration.getSecretKey(), "secret"
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePostProcessor.processAfter(config));
        assertNotNull(config.getToken());
    }

    @Test
    public void processNullSigningKeyTest() {
        Document document = Document
                .builder()
                .title("test.docx")
                .key("asdfd")
                .url("https://example.com")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .custom(Map.of(
                        configuration.getAfterMapKey(), new HashMap<>(){{
                            put(configuration.getSecretKey(), null);
                        }}
                ))
                .build();
        assertDoesNotThrow(() -> this.configOnlyofficePostProcessor.processAfter(config));
        assertNull(config.getToken());
    }
}
