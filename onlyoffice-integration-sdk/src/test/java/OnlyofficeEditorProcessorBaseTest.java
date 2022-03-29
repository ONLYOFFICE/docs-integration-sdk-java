import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficeEditorProcessorBase;
import core.util.OnlyofficeConfigUtil;
import core.util.OnlyofficeConfigUtilBase;
import core.util.OnlyofficeFileUtilBase;
import exception.OnlyofficeInvalidParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnlyofficeEditorProcessorBaseTest {
    private final OnlyofficeFileUtilBase fileUtil = new OnlyofficeFileUtilBase();
    private final OnlyofficeConfigUtil configUtil = new OnlyofficeConfigUtilBase(fileUtil);
    private final OnlyofficeEditorProcessorBase editorProcessorBase = new OnlyofficeEditorProcessorBase(configUtil);

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
}
