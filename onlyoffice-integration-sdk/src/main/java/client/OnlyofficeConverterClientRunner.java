package client;

import core.model.converter.request.ConverterRequest;
import core.security.model.OnlyofficeDocumentServerCredentials;
import exception.OnlyofficeInvalidParameterException;

import java.io.IOException;

public interface OnlyofficeConverterClientRunner {
    /**
     *
     * @param request
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    void convertAndUpload(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param request
     * @param credentials
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    void convertAndUpload(ConverterRequest request, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;
}
