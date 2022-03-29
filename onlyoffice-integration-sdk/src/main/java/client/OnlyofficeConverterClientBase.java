package client;


import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterAsyncResponse;
import core.model.converter.response.ConverterResponse;
import core.security.OnlyofficeJwtManager;
import core.security.model.OnlyofficeDocumentServerCredentials;
import core.util.OnlyofficeValidationCaller;
import exception.OnlyofficeInvalidParameterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class OnlyofficeConverterClientBase implements OnlyofficeConverterClient {
    private final OnlyofficeJwtManager jwtManager;
    private final ObjectMapper mapper;
    private int connectTimeout = 5000;
    private int responseTimeout = 24000;

    /**
     *
     * @param connectTimeout
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     *
     * @param responseTimeout
     */
    public void setResponseTimeout(int responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    public ConverterResponse convert(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterException {
        request.setAsync(false);
        OnlyofficeValidationCaller.validate(request);
        return execute(request, request.getAddress());
    }

    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    public ConverterAsyncResponse convertAsync(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterException {
        request.setAsync(true);
        OnlyofficeValidationCaller.validate(request);
        return executeAsync(request, request.getAddress());
    }

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    public ConverterResponse convert(ConverterRequest request, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException {
        request.setAsync(false);
        OnlyofficeValidationCaller.validate(request);
        return execute(request, request.getAddress(), credentials);
    }

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    public ConverterAsyncResponse convertAsync(ConverterRequest request, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException {
        request.setAsync(true);
        OnlyofficeValidationCaller.validate(request);
        return executeAsync(request, request.getAddress(), credentials);
    }

    /**
     *
     * @param request
     * @throws OnlyofficeInvalidParameterException
     */
    private void validateConversionTypes(ConverterRequest request) throws OnlyofficeInvalidParameterException {
        if (!request.getFiletype().getConvertTypes().contains(request.getOutputtype().getExtension()))
            throw new OnlyofficeInvalidParameterException(String
                    .format(
                            "Can not convert file of type %s to type %s",
                            request.getFiletype().getExtension(),
                            request.getOutputtype().getExtension()
                    )
            );
    }

    /**
     *
     * @param request
     * @param address
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    private ConverterResponse execute(ConverterRequest request, URI address) throws IOException, OnlyofficeInvalidParameterException {
        this.validateConversionTypes(request);
        String json = this.mapper.writeValueAsString(request);
        String textResponse = Request.post(address)
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .setHeader("Accept", "application/json")
                .execute().returnContent().asString();
        return this.mapper.readValue(textResponse, ConverterResponse.class);
    }

    /**
     *
     * @param request
     * @param address
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    private ConverterResponse execute(ConverterRequest request, URI address, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException {
        this.validateConversionTypes(request);
        Date expiresAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(1));
        String token = this.jwtManager.sign(request, credentials.getSecret(), expiresAt)
                .orElseThrow(() -> new OnlyofficeInvalidParameterException("Could not create a JWT"));
        request.setToken(token);
        String json = this.mapper.writeValueAsString(request);
        String textResponse = Request.post(address)
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .setHeader("Accept", "application/json")
                .setHeader(credentials.getHeader().trim(), credentials.getPrefix().trim()+" "+token)
                .execute().returnContent().asString();
        return this.mapper.readValue(textResponse, ConverterResponse.class);
    }

    /**
     *
     * @param request
     * @param address
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    private ConverterAsyncResponse executeAsync(ConverterRequest request, URI address) throws IOException, OnlyofficeInvalidParameterException {
        this.validateConversionTypes(request);
        String json = this.mapper.writeValueAsString(request);
        Request.post(address)
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .setHeader("Accept", "application/json")
                .execute().returnContent().asString();
        return new ConverterAsyncResponse(json, address);
    }

    /**
     *
     * @param request
     * @param address
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    private ConverterAsyncResponse executeAsync(ConverterRequest request, URI address, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException {
        this.validateConversionTypes(request);
        Date expiresAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(1));
        String token = this.jwtManager.sign(request, credentials.getSecret(), expiresAt)
                .orElseThrow(() -> new OnlyofficeInvalidParameterException("Could not create a JWT"));
        request.setToken(token);
        String json = this.mapper.writeValueAsString(request);
        Request.post(address)
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .setHeader("Accept", "application/json")
                .setHeader(credentials.getHeader().trim(), credentials.getPrefix().trim()+" "+token)
                .execute().returnContent().asString();
        return new ConverterAsyncResponse(json, address);
    }
}
