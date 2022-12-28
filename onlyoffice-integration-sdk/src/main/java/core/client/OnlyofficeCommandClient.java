package core.client;

import core.model.command.*;
import core.model.command.license.request.CommandLicenseRequest;
import core.model.command.license.response.CommandLicenseResponse;
import core.model.command.meta.CommandMetaRequest;
import core.model.common.Credentials;
import exception.OnlyofficeInvalidParameterRuntimeException;

import java.io.IOException;

public interface OnlyofficeCommandClient {
    /**
     *
     * @param commandRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse drop(CommandDropRequest commandRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param commandRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse drop(CommandDropRequest commandRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param infoRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse info(CommandInfoRequest infoRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param infoRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse info(CommandInfoRequest infoRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param forcesaveRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse forcesave(CommandForcesaveRequest forcesaveRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param forcesaveRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse forcesave(CommandForcesaveRequest forcesaveRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param versionRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandVersionResponse version(CommandVersionRequest versionRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param versionRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandVersionResponse version(CommandVersionRequest versionRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param metaRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse meta(CommandMetaRequest metaRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param metaRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandResponse meta(CommandMetaRequest metaRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param licenseRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandLicenseResponse license(CommandLicenseRequest licenseRequest) throws IOException, OnlyofficeInvalidParameterRuntimeException;

    /**
     *
     * @param licenseRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    CommandLicenseResponse license(CommandLicenseRequest licenseRequest, Credentials credentials) throws IOException, OnlyofficeInvalidParameterRuntimeException;
}