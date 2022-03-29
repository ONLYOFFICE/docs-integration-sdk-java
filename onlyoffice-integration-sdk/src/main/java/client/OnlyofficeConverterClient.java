package client;

import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterAsyncResponse;
import core.model.converter.response.ConverterResponse;
import core.security.model.OnlyofficeDocumentServerCredentials;
import exception.OnlyofficeInvalidParameterException;

import java.io.IOException;

public interface OnlyofficeConverterClient {
    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    ConverterResponse convert(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    ConverterAsyncResponse convertAsync(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    ConverterResponse convert(ConverterRequest request, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    ConverterAsyncResponse convertAsync(ConverterRequest request, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;
}
