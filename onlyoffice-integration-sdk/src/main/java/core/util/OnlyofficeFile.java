package core.util;

import core.model.converter.format.DocumentType;
import exception.OnlyofficeInvalidParameterRuntimeException;

public interface OnlyofficeFile {
    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    DocumentType findDocumentType(String fileName) throws OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    String findFileType(String fileName) throws OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    Boolean isEditable(String fileName) throws OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param fileName
     * @return
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    Boolean fillFormsAllowed(String fileName) throws OnlyofficeInvalidParameterRuntimeException;
}
