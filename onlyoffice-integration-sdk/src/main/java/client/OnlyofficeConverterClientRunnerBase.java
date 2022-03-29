package client;


import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterResponse;
import core.security.model.OnlyofficeDocumentServerCredentials;
import core.uploader.OnlyofficeFileUploader;
import core.uploader.OnlyofficeFileUploaderType;
import exception.OnlyofficeInvalidParameterException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class OnlyofficeConverterClientRunnerBase implements OnlyofficeConverterClientRunner {
    private final List<OnlyofficeConverterUploader> fileUploaders;
    private final OnlyofficeConverterClient converterClient;

    /**
     *
     * @param fileUploaders
     * @param converterClient
     */
    public OnlyofficeConverterClientRunnerBase(List<OnlyofficeConverterUploader> fileUploaders, OnlyofficeConverterClient converterClient) {
        this.converterClient = converterClient;
        this.fileUploaders = fileUploaders.stream()
                .filter(uploader -> uploader.getType().equals(OnlyofficeFileUploaderType.FILE))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param request
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    public void convertAndUpload(ConverterRequest request) throws IOException, OnlyofficeInvalidParameterException {
        ConverterResponse response = this.converterClient.convert(request);
        for (OnlyofficeFileUploader uploader : fileUploaders) {
            doUpload(request, response, uploader);
        }
    }

    /**
     *
     * @param request
     * @param credentials
     * @throws IOException
     * @throws OnlyofficeInvalidParameterException
     */
    public void convertAndUpload(ConverterRequest request, OnlyofficeDocumentServerCredentials credentials) throws IOException, OnlyofficeInvalidParameterException {
        ConverterResponse response = this.converterClient.convert(request, credentials);
        for (OnlyofficeFileUploader uploader : fileUploaders) {
            doUpload(request, response, uploader);
        }
    }

    /**
     *
     * @param request
     * @param response
     * @param uploader
     * @throws IOException
     */
    private void doUpload(ConverterRequest request, ConverterResponse response, OnlyofficeFileUploader uploader) throws IOException {
        try (InputStream inputStream = new URL(response.getFileUrl()).openStream()) {
            uploader.upload(request, inputStream);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
