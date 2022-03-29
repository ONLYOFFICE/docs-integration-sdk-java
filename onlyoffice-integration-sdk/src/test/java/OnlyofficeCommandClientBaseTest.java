import client.OnlyofficeCommandClientBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.command.*;
import core.model.command.license.request.CommandLicenseRequest;
import core.model.command.license.response.CommandLicenseResponse;
import core.model.command.meta.CommandMetaRequest;
import core.model.command.meta.Meta;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import core.security.model.OnlyofficeDocumentServerCredentials;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeCommandClientBaseTest {
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeCommandClientBase commandClient = new OnlyofficeCommandClientBase(jwtManager, objectMapper);
    private final OnlyofficeDocumentServerCredentials credentials = OnlyofficeDocumentServerCredentials
            .builder()
            .secret(System.getenv("secret"))
            .header(System.getenv("header"))
            .build();
    private final String key = UUID.randomUUID().toString();
    private final URI addressJWT = new URI(System.getenv("jwt_document_server")+"/coauthoring/CommandService.ashx");
    private final URI address = new URI(System.getenv("document_server")+"/coauthoring/CommandService.ashx");

    public OnlyofficeCommandClientBaseTest() throws URISyntaxException {
    }

    @Test
    @SneakyThrows
    public void dropTest() {
        CommandDropRequest dropRequest = CommandDropRequest.builder().address(address).key(key).build();
        CommandResponse response = this.commandClient.drop(dropRequest);
        assertNotNull(response);
        assertEquals(response.getKey(), key);
    }

    @Test
    @SneakyThrows
    public void dropCredentialsTest() {
        CommandDropRequest dropRequest = CommandDropRequest.builder().address(addressJWT).key(key).build();
        CommandResponse response = this.commandClient.drop(dropRequest, credentials);
        assertNotNull(response);
        assertEquals(response.getKey(), key);
        assertNotEquals(6, response.getError());
    }

    @Test
    @SneakyThrows
    public void infoTest() {
        CommandInfoRequest infoRequest = CommandInfoRequest.builder().address(address).key(key).build();
        CommandResponse response = this.commandClient.info(infoRequest);
        assertNotNull(response);
        assertEquals(response.getKey(), key);
    }

    @Test
    @SneakyThrows
    public void infoCredentialsTest() {
        CommandInfoRequest infoRequest = CommandInfoRequest.builder().address(addressJWT).key(key).build();
        CommandResponse response = this.commandClient.info(infoRequest, credentials);
        assertNotNull(response);
        assertEquals(response.getKey(), key);
        assertNotEquals(6, response.getError());
    }

    @Test
    @SneakyThrows
    public void forcesaveTest() {
        CommandForcesaveRequest forcesaveRequest = CommandForcesaveRequest.builder().address(address).key(key).build();
        CommandResponse response = this.commandClient.forcesave(forcesaveRequest);
        assertNotNull(response);
        assertEquals(response.getKey(), key);
    }

    @Test
    @SneakyThrows
    public void forcesaveCredentialsTest() {
        CommandForcesaveRequest forcesaveRequest = CommandForcesaveRequest.builder().address(addressJWT).key(key).build();
        CommandResponse response = this.commandClient.forcesave(forcesaveRequest, credentials);
        assertNotNull(response);
        assertEquals(response.getKey(), key);
        assertNotEquals(6, response.getError());
    }

    @Test
    @SneakyThrows
    public void versionTest() {
        CommandVersionRequest versionRequest = CommandVersionRequest.builder().address(address).build();
        CommandVersionResponse response = this.commandClient.version(versionRequest);
        assertNotNull(response);
    }

    @Test
    @SneakyThrows
    public void versionCredentialsTest() {
        CommandVersionRequest versionRequest = CommandVersionRequest.builder().address(addressJWT).build();
        CommandVersionResponse response = this.commandClient.version(versionRequest, credentials);
        assertNotNull(response);
        assertNotEquals(6, response.getError());
    }

    @Test
    @SneakyThrows
    public void metaTest() {
        Meta meta = Meta
                .builder()
                .title("title.docx")
                .build();
        CommandMetaRequest metaRequest = CommandMetaRequest.builder().address(address).key(key).meta(meta).build();
        CommandResponse response = this.commandClient.meta(metaRequest);
        assertNotNull(response);
    }

    @Test
    @SneakyThrows
    public void metaCredentialsTest() {
        Meta meta = Meta
                .builder()
                .title("title.docx")
                .build();
        CommandMetaRequest metaRequest = CommandMetaRequest.builder().address(addressJWT).key(key).meta(meta).build();
        CommandResponse response = this.commandClient.meta(metaRequest, credentials);
        assertNotNull(response);
        assertNotEquals(6, response.getError());
    }

    @Test
    @SneakyThrows
    public void licenseTest() {
        CommandLicenseRequest licenseRequest = CommandLicenseRequest.builder().address(address).build();
        CommandLicenseResponse response = this.commandClient.license(licenseRequest);
        assertNotNull(response);
    }

    @Test
    @SneakyThrows
    public void licenseCredentialsTest() {
        CommandLicenseRequest licenseRequest = CommandLicenseRequest.builder().address(addressJWT).build();
        CommandLicenseResponse response = this.commandClient.license(licenseRequest, credentials);
        assertNotNull(response);
        assertNotEquals(6, response.getError());
    }
}
