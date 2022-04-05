package base.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.client.OnlyofficeCommandClient;
import core.model.Credentials;
import core.model.command.*;
import core.model.command.license.request.CommandLicenseRequest;
import core.model.command.license.response.CommandLicenseResponse;
import core.model.command.meta.CommandMetaRequest;
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
public class OnlyofficeDefaultCommandClient implements OnlyofficeCommandClient {
    @Getter
    @Setter
    private int connectTimeout = 2000;
    @Getter
    @Setter
    private int responseTimeout = 4000;
    private final OnlyofficeJwtSecurity jwtManager;
    private final ObjectMapper mapper;

    /**
     *
     * @param commandRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse drop(CommandDropRequest commandRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(commandRequest);
        String json = this.mapper.writeValueAsString(commandRequest);
        return this.executeCommand(json, commandRequest.getAddress());
    }

    /**
     *
     * @param commandRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse drop(CommandDropRequest commandRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(commandRequest);
        return this.executeCommand(commandRequest, commandRequest.getAddress(), credentials);
    }

    /**
     *
     * @param infoRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse info(CommandInfoRequest infoRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(infoRequest);
        String json = this.mapper.writeValueAsString(infoRequest);
        return this.executeCommand(json, infoRequest.getAddress());
    }

    /**
     *
     * @param infoRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse info(CommandInfoRequest infoRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(infoRequest);
        return this.executeCommand(infoRequest, infoRequest.getAddress(), credentials);
    }

    /**
     *
     * @param forcesaveRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse forcesave(CommandForcesaveRequest forcesaveRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(forcesaveRequest);
        String json = this.mapper.writeValueAsString(forcesaveRequest);
        return this.executeCommand(json, forcesaveRequest.getAddress());
    }

    /**
     *
     * @param forcesaveRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse forcesave(CommandForcesaveRequest forcesaveRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(forcesaveRequest);
        return this.executeCommand(forcesaveRequest, forcesaveRequest.getAddress(), credentials);
    }

    /**
     *
     * @param versionRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandVersionResponse version(CommandVersionRequest versionRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(versionRequest);
        String json = this.mapper.writeValueAsString(versionRequest);
        String response = Request.post(versionRequest.getAddress())
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.SECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.SECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();
        return this.mapper.readValue(response, CommandVersionResponse.class);
    }

    /**
     *
     * @param versionRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandVersionResponse version(CommandVersionRequest versionRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(versionRequest);
        Date expiresAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(2));
        String token = this.jwtManager.sign(versionRequest, credentials.getSecret(), expiresAt)
                .orElseThrow(() -> new IOException("Could not generate a jwt token"));
        versionRequest.setToken(token);
        String json = this.mapper.writeValueAsString(versionRequest);
        String response = Request.post(versionRequest.getAddress())
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .setHeader(credentials.getHeader(), credentials.getPrefix()+" "+token)
                .execute().returnContent().asString();
        return this.mapper.readValue(response, CommandVersionResponse.class);
    }

    /**
     *
     * @param metaRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse meta(CommandMetaRequest metaRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(metaRequest);
        String json = this.mapper.writeValueAsString(metaRequest);
        return this.executeCommand(json, metaRequest.getAddress());
    }

    /**
     *
     * @param metaRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandResponse meta(CommandMetaRequest metaRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(metaRequest);
        return this.executeCommand(metaRequest, metaRequest.getAddress(), credentials);
    }

    /**
     *
     * @param licenseRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandLicenseResponse license(CommandLicenseRequest licenseRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(licenseRequest);
        String json = this.mapper.writeValueAsString(licenseRequest);
        String response = Request.post(licenseRequest.getAddress())
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();
        return this.mapper.readValue(response, CommandLicenseResponse.class);
    }

    /**
     *
     * @param licenseRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public CommandLicenseResponse license(CommandLicenseRequest licenseRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException {
        OnlyofficeModelValidator.validate(licenseRequest);
        Date expiresAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(2));
        String token = this.jwtManager.sign(licenseRequest, credentials.getSecret(), expiresAt)
                .orElseThrow(() -> new IOException("Could not generate a jwt token"));
        licenseRequest.setToken(token);
        String json = this.mapper.writeValueAsString(licenseRequest);
        String response = Request.post(licenseRequest.getAddress())
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .setHeader(credentials.getHeader().trim(), credentials.getPrefix().trim()+" "+token)
                .execute().returnContent().asString();
        return this.mapper.readValue(response, CommandLicenseResponse.class);
    }

    /**
     *
     * @param json
     * @param address
     * @return
     * @throws IOException
     */
    private CommandResponse executeCommand(String json, URI address) throws IOException {
        String response = Request.post(address)
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();
        return this.mapper.readValue(response, CommandResponse.class);
    }

    /**
     *
     * @param base
     * @param address
     * @param credentials
     * @return
     * @throws IOException
     */
    private CommandResponse executeCommand(CommandAuthorizedBase base, URI address, Credentials credentials) throws IOException {
        Date expiresAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(2));
        String token = this.jwtManager.sign(base, credentials.getSecret(), expiresAt)
                .orElseThrow(() -> new IOException("Could not generate a jwt token"));
        base.setToken(token);
        String json = this.mapper.writeValueAsString(base);
        String response = Request.post(address)
                .connectTimeout(Timeout.of(this.connectTimeout, TimeUnit.MILLISECONDS))
                .responseTimeout(Timeout.of(this.responseTimeout, TimeUnit.MILLISECONDS))
                .bodyString(json, ContentType.APPLICATION_JSON)
                .setHeader(credentials.getHeader().trim(), credentials.getPrefix().trim()+" "+token)
                .execute().returnContent().asString();
        return this.mapper.readValue(response, CommandResponse.class);
    }
}
