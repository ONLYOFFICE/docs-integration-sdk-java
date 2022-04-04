package base.util;

import core.model.converter.format.DocumentType;
import core.util.OnlyofficeFile;
import exception.OnlyofficeInvalidParameterRuntimeException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OnlyofficeFileUtil implements OnlyofficeFile {
    private Set<String> editable = Stream.of("docx", "pptx", "xlsx")
            .collect(Collectors.toSet());
    private Set<String> fillAllowed = Stream.of("oform","docx")
            .collect(Collectors.toSet());
    private Set<String> word = Stream.of("pdf", "doc", "docx", "docm", "dot", "dotx", "dotm", "odt", "fodt", "ott", "rtf", "txt", "fb2", "epub")
            .collect(Collectors.toSet());
    private Set<String> cell = Stream.of("xls", "xlsx", "xlsm", "xlt", "xltx", "xltm", "ods", "fods", "ots", "csv")
            .collect(Collectors.toSet());
    private Set<String> slide = Stream.of("pps", "ppsx", "ppsm", "ppt", "pptx", "pptm", "pot", "potx", "potm", "odp", "fodp", "otp")
            .collect(Collectors.toSet());

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public DocumentType findDocumentType(String fileName) throws OnlyofficeInvalidParameterRuntimeException {
        String fileType = this.findFileType(fileName);
        if (this.cell.contains(fileType)) return DocumentType.CELL;
        if (this.slide.contains(fileType)) return DocumentType.SLIDE;
        return DocumentType.WORD;
    }

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public String findFileType(String fileName) throws OnlyofficeInvalidParameterRuntimeException {
        String[] parts = fileName.split("\\.");
        if (parts.length != 2)
            throw new OnlyofficeInvalidParameterRuntimeException(String.format("Expected file name. Got unknown string: %s", fileName));
        return parts[1];
    }

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public Boolean isEditable(String fileName) throws OnlyofficeInvalidParameterRuntimeException {
        String fileType = this.findFileType(fileName);
        return this.editable.contains(fileType);
    }

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public Boolean fillFormsAllowed(String fileName) throws OnlyofficeInvalidParameterRuntimeException {
        String fileType = this.findFileType(fileName);
        return this.fillAllowed.contains(fileType);
    }
}
