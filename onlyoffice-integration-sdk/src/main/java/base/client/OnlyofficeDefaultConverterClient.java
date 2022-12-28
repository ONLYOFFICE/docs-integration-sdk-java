package base.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import core.client.OnlyofficeConverterClient;
import core.model.common.Credentials;
import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterAsyncResponse;
import core.model.converter.response.ConverterResponse;
import core.security.OnlyofficeJwtSecurity;
import core.util.OnlyofficeModelValidator;
import exception.OnlyofficeInvalidParameterRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class OnlyofficeDefaultConverterClient implements OnlyofficeConverterClient {
    @Getter
    @Setter
    private int connectTimeout = 5000;
    @Getter
    @Setter
    private int responseTimeout = 24000;
    private final OnlyofficeJwtSecurity jwtManager;
    private final ObjectMapper mapper;

    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public ConverterResponse convert(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        request.setAsync(false);
        OnlyofficeModelValidator.validate(request);
        return execute(request, getConverterURI(request.getAddress()));
    }

    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public ConverterAsyncResponse convertAsync(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        request.setAsync(true);
        OnlyofficeModelValidator.validate(request);
        return executeAsync(request, getConverterURI(request.getAddress()));
    }

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public ConverterResponse convert(ConverterRequest request, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        request.setAsync(false);
        OnlyofficeModelValidator.validate(request);
        return execute(request, getConverterURI(request.getAddress()), credentials);
    }

    /**
     *
     * @param request
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public ConverterAsyncResponse convertAsync(ConverterRequest request, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        request.setAsync(true);
        OnlyofficeModelValidator.validate(request);
        return executeAsync(request, getConverterURI(request.getAddress()), credentials);
    }

    /**
     *
     * @param base
     * @return
     */
    private URI getConverterURI(URI base) {
        if (!base.getPath().endsWith("/ConvertService.ashx")) {
            URI valid = base.resolve("/ConvertService.ashx").normalize();
            return valid;
        }

        return base.normalize();
    }

    /**
     *
     * @param request
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    private void validateConversionTypes(ConverterRequest request) throws OnlyofficeInvalidParameterRuntimeException {
        if (!request.getFiletype().getConvertTypes().contains(request.getOutputtype().getExtension()))
            throw new OnlyofficeInvalidParameterRuntimeException(String
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
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    private ConverterResponse execute(ConverterRequest request, URI address) throws IOException, OnlyofficeInvalidParameterRuntimeException {
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
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    private ConverterResponse execute(ConverterRequest request, URI address, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        this.validateConversionTypes(request);
        Date expiresAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(1));
        String token = this.jwtManager.sign(request, credentials.getSecret(), expiresAt)
                .orElseThrow(() -> new OnlyofficeInvalidParameterRuntimeException("Could not create a JWT"));
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
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    private ConverterAsyncResponse executeAsync(ConverterRequest request, URI address) throws IOException, OnlyofficeInvalidParameterRuntimeException {
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
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    private ConverterAsyncResponse executeAsync(ConverterRequest request, URI address, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        this.validateConversionTypes(request);
        Date expiresAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(1));
        String token = this.jwtManager.sign(request, credentials.getSecret(), expiresAt)
                .orElseThrow(() -> new OnlyofficeInvalidParameterRuntimeException("Could not create a JWT"));
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
