import core.model.converter.format.DocumentType;
import org.junit.jupiter.api.Test;
import core.util.OnlyofficeFileUtilBase;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeFileUtilBaseTest {
    private OnlyofficeFileUtilBase fileUtilBase = new OnlyofficeFileUtilBase();

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
