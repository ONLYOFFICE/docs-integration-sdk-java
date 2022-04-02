package core.util;

import core.model.converter.format.DocumentType;
import exception.OnlyofficeInvalidParameterException;

public interface OnlyofficeFileUtil {
    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterException
     */
    DocumentType findDocumentType(String fileName) throws OnlyofficeInvalidParameterException;

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterException
     */
    String findFileType(String fileName) throws OnlyofficeInvalidParameterException;

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterException
     */
    Boolean isEditable(String fileName) throws OnlyofficeInvalidParameterException;

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterException
     */
    Boolean fillFormsAllowed(String fileName) throws OnlyofficeInvalidParameterException;
}
