import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import org.junit.jupiter.api.Test;
import core.util.OnlyofficeConfigUtil;
import core.util.OnlyofficeConfigUtilBase;
import core.util.OnlyofficeFileUtil;
import core.util.OnlyofficeFileUtilBase;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeConfigUtilBaseTest {
    private final OnlyofficeFileUtil fileUtil = new OnlyofficeFileUtilBase();
    private final OnlyofficeConfigUtil configUtil = new OnlyofficeConfigUtilBase(fileUtil);
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
        config = this.configUtil.sanitizeConfig(config);
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
        config = this.configUtil.sanitizeConfig(config);
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
        config = this.configUtil.sanitizeConfig(config);
        assertEquals("view", config.getEditorConfig().getMode());
    }
}
