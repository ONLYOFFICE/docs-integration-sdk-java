import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

//TODO: Replace with mocks
public class OnlyofficeConfigTest {
    private final OnlyofficeFile fileUtil = new OnlyofficeFileUtil();
    private final OnlyofficeConfig configUtil = new OnlyofficeConfigUtil(fileUtil);
    private final Editor editor = Editor
            .builder()
            .callbackUrl("https://example.com")
            .build();

    @Test
    public void sanitizeDocxConfig() {
        Document document = Document
                .builder()
                .key("12345")
                .url("https://example.com")
                .title("title.docx")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .build();
        this.configUtil.sanitizeConfig(config);
        assertEquals("edit", config.getEditorConfig().getMode());
    }

    @Test
    public void sanitizeNotSupportedExtConfig() {
        Document document = Document
                .builder()
                .key("12345")
                .url("https://example.com")
                .title("title.asd")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .build();
        this.configUtil.sanitizeConfig(config);
        assertNull(config.getEditorConfig().getMode());
    }

    @Test
    public void sanitizeNonEditableExtConfig() {
        Document document = Document
                .builder()
                .key("12345")
                .url("https://example.com")
                .title("title.doc")
                .build();
        Config config = Config
                .builder()
                .document(document)
                .editorConfig(editor)
                .build();
        this.configUtil.sanitizeConfig(config);
        assertEquals("view", config.getEditorConfig().getMode());
    }
}
