import base.processor.OnlyofficeDefaultEditorProcessor;
import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficeEditorProcessor;
import core.security.OnlyofficeJwtSecurityManager;
import core.util.OnlyofficeConfig;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Replace with mocks
public class OnlyofficeDefaultEditorProcessorTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeFileUtil fileUtil = new OnlyofficeFileUtil();
    private final OnlyofficeConfig configUtil = new OnlyofficeConfigUtil(fileUtil);
    private final OnlyofficeJwtSecurityManager jwtManager = new OnlyofficeJwtSecurityManager(objectMapper);
    private final OnlyofficeEditorProcessor editorProcessorBase = new OnlyofficeDefaultEditorProcessor(configUtil, jwtManager);

    @Test
    public void processNullPayloadTest() {
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.editorProcessorBase.process(null));
    }

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

        assertThrows(OnlyofficeInvalidParameterRuntimeException.class, () -> this.editorProcessorBase.process(config));
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

        assertDoesNotThrow(() -> this.editorProcessorBase.process(config));
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
        assertDoesNotThrow(() -> this.editorProcessorBase.process(config));
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
        assertDoesNotThrow(() -> this.editorProcessorBase.process(config));
        assertNull(config.getToken());
    }
}
