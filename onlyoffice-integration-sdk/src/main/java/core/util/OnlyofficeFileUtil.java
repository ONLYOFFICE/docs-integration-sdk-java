package core.util;

import core.model.converter.format.DocumentType;
import exception.OnlyofficeInvalidParameterException;

public interface OnlyofficeFileUtil {
    DocumentType findDocumentType(String fileName) throws OnlyofficeInvalidParameterException;
    String findFileType(String fileName) throws OnlyofficeInvalidParameterException;
    Boolean isEditable(String fileName) throws OnlyofficeInvalidParameterException;
    Boolean fillFormsAllowed(String fileName) throws OnlyofficeInvalidParameterException;
}
