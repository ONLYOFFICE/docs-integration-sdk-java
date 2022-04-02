import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.OnlyofficeEditorProcessorBase;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import core.util.OnlyofficeConfigUtil;
import core.util.OnlyofficeConfigUtilBase;
import core.util.OnlyofficeFileUtilBase;
import exception.OnlyofficeInvalidParameterException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Replace with mocks
public class OnlyofficeEditorProcessorBaseTest {
    private final OnlyofficeFileUtilBase fileUtil = new OnlyofficeFileUtilBase();
    private final OnlyofficeConfigUtil configUtil = new OnlyofficeConfigUtilBase(fileUtil);
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final OnlyofficeEditorProcessorBase editorProcessorBase = new OnlyofficeEditorProcessorBase(configUtil, jwtManager);

    @Test
    public void processEditorConfigInvalidPayloadTest() {
        Document document = Document
                .builder()
                .title("test.docx")
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

        assertThrows(OnlyofficeInvalidParameterException.class, () -> this.editorProcessorBase.processEditorConfig(config));
    }

    @Test
    public void processEditorConfigValidPayloadTest() {
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

        assertDoesNotThrow(() -> this.editorProcessorBase.processEditorConfig(config));
    }

    @Test
    public void processSignConfigTest() {
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
                .secret("secret")
                .build();
        assertDoesNotThrow(() -> this.editorProcessorBase.processEditorConfig(config));
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
                .secret(null)
                .build();
        assertDoesNotThrow(() -> this.editorProcessorBase.processEditorConfig(config));
        assertNull(config.getToken());
    }
}
