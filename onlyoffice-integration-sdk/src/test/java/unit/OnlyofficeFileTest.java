package unit;

import core.model.converter.format.DocumentType;
import base.util.OnlyofficeFileUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnlyofficeFileTest {
    private OnlyofficeFileUtil fileUtilBase = new OnlyofficeFileUtil();

    @Test
    public void getFileTypeTest() {
        String fileName = "test.docx";
        String fileType = this.fileUtilBase.findFileType(fileName);
        assertEquals(fileType, "docx");
    }

    @Test
    public void getDocumentTypeTest() {
        String fileName = "test.docx";
        DocumentType docType = this.fileUtilBase.findDocumentType(fileName);
        assertEquals(docType.getType(), "word");
    }
}
