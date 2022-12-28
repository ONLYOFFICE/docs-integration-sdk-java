package integration;

import base.client.OnlyofficeDefaultConverterClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.client.OnlyofficeConverterClient;
import core.model.common.Credentials;
import core.model.converter.format.Format;
import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterAsyncResponse;
import core.model.converter.response.ConverterResponse;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeInvalidParameterRuntimeException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeConvertClientTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeJwtSecurity jwtSecurity = new OnlyofficeJwtSecurityManager(objectMapper);
    private final OnlyofficeConverterClient converterClient = new OnlyofficeDefaultConverterClient(jwtSecurity, objectMapper);
    private final URI addressJwt = new URI(System.getenv("jwt_document_server"));
    private final URI address = new URI(System.getenv("document_server"));
    private final String fileUrl = System.getenv("file_url");
    private final Credentials credentials = Credentials
            .builder()
            .secret(System.getenv("secret"))
            .header(System.getenv("header"))
            .build();

    public OnlyofficeConvertClientTest() throws URISyntaxException {
    }

    @Test
    @SneakyThrows
    public void convertSyncInvalidOutputTypeTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(address)
                .filetype(Format.DOC)
                .outputtype(Format.XLS)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        assertThrows(OnlyofficeInvalidParameterRuntimeException.class, () -> this.converterClient.convert(request));
    }

    @Test
    @SneakyThrows
    public void convertAsyncInvalidOutputTypeTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(address)
                .filetype(Format.DOC)
                .outputtype(Format.XLSX)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        assertThrows(OnlyofficeInvalidParameterRuntimeException.class, () -> this.converterClient.convertAsync(request));
    }

    @Test
    @SneakyThrows
    public void convertSyncTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(address)
                .filetype(Format.DOC)
                .outputtype(Format.DOCX)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        ConverterResponse response = this.converterClient.convert(request);
        assertEquals(0, response.getError());
    }

    @Test
    @SneakyThrows
    public void convertAsyncTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(address)
                .filetype(Format.DOC)
                .outputtype(Format.DOCX)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        ConverterAsyncResponse response = this.converterClient.convertAsync(request);
        assertNotNull(response.getNow().getFileUrl());
        assertEquals(100, response.getNow().getPercent());
    }

    @Test
    @SneakyThrows
    public void convertSyncValidJwtTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(addressJwt)
                .filetype(Format.DOC)
                .outputtype(Format.DOCX)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        ConverterResponse response = this.converterClient.convert(request, credentials);
        assertEquals(0, response.getError());
    }

    @Test
    @SneakyThrows
    public void convertSyncInvalidJwtTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(addressJwt)
                .filetype(Format.DOC)
                .outputtype(Format.DOCX)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        ConverterResponse response = this.converterClient.convert(request);
        assertEquals(-8, response.getError());
    }

    @Test
    @SneakyThrows
    public void convertAsyncValidJwtTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(addressJwt)
                .filetype(Format.DOC)
                .outputtype(Format.DOCX)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        ConverterAsyncResponse response = this.converterClient.convertAsync(request, credentials);
        assertNotNull(response.getNow().getFileUrl());
        assertEquals(0, response.getNow().getError());
    }

    @Test
    @SneakyThrows
    public void convertAsyncInvalidJwtTest() {
        ConverterRequest request = ConverterRequest
                .builder()
                .address(addressJwt)
                .filetype(Format.DOC)
                .outputtype(Format.DOCX)
                .key(UUID.randomUUID().toString())
                .url(fileUrl)
                .build();
        ConverterAsyncResponse response = this.converterClient.convertAsync(request);
        assertEquals(-8, response.getNow().getError());
    }
}
