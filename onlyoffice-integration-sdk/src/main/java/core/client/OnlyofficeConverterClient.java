package core.client;

import core.model.common.Credentials;
import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterAsyncResponse;
import core.model.converter.response.ConverterResponse;
import exception.OnlyofficeInvalidParameterRuntimeException;

import java.io.IOException;

public interface OnlyofficeConverterClient {
    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    ConverterResponse convert(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    ConverterAsyncResponse convertAsync(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    ConverterResponse convert(ConverterRequest request, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    ConverterAsyncResponse convertAsync(ConverterRequest request, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;
}