package integration;

import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficeEditorProcessor;
import core.util.OnlyofficeConfig;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeEditorProcessorTest {
    private final OnlyofficeFileUtil fileUtil = new OnlyofficeFileUtil();
    private final OnlyofficeConfig configUtil = new OnlyofficeConfigUtil(fileUtil);
    private final OnlyofficeEditorProcessor editorProcessorBase = new OnlyofficeEditorProcessor(configUtil);

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
}
