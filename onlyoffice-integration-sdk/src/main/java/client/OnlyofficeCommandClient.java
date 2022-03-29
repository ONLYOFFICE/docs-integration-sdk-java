package client;

import core.model.command.*;
import core.model.command.license.request.CommandLicenseRequest;
import core.model.command.license.response.CommandLicenseResponse;
import core.model.command.meta.CommandMetaRequest;
import core.security.model.OnlyofficeDocumentServerCredentials;
import exception.OnlyofficeInvalidParameterException;

import java.io.IOException;

public interface OnlyofficeCommandClient {
    /**
     *
     * @param commandRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse drop(CommandDropRequest commandRequest) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param commandRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse drop(CommandDropRequest commandRequest, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param infoRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse info(CommandInfoRequest infoRequest) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param infoRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse info(CommandInfoRequest infoRequest, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param forcesaveRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse forcesave(CommandForcesaveRequest forcesaveRequest) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param forcesaveRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse forcesave(CommandForcesaveRequest forcesaveRequest, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param versionRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandVersionResponse version(CommandVersionRequest versionRequest) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param versionRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandVersionResponse version(CommandVersionRequest versionRequest, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param metaRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse meta(CommandMetaRequest metaRequest) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param metaRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandResponse meta(CommandMetaRequest metaRequest, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param licenseRequest
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandLicenseResponse license(CommandLicenseRequest licenseRequest) throws IOException, OnlyofficeInvalidParameterException;

    /**
     *
     * @param licenseRequest
     * @param credentials
     * @return
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    CommandLicenseResponse license(CommandLicenseRequest licenseRequest, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException;
}
