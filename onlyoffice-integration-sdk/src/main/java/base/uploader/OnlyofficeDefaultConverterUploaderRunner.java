package base.uploader;


import core.client.OnlyofficeConverterClient;
import core.model.Credentials;
import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterResponse;
import core.uploader.OnlyofficeUploader;
import core.uploader.OnlyofficeUploaderRunner;
import core.uploader.OnlyofficeUploaderType;
import exception.OnlyofficeRunnerRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class OnlyofficeDefaultConverterUploaderRunner implements OnlyofficeUploaderRunner<ConverterRequest> {
    private final List<OnlyofficeUploader<ConverterRequest>> fileUploaders;
    private final OnlyofficeConverterClient converterClient;

    /**
     *
     * @param fileUploaders
     * @param converterClient
     */
    public OnlyofficeDefaultConverterUploaderRunner(List<OnlyofficeUploader<ConverterRequest>> fileUploaders, OnlyofficeConverterClient converterClient) {
        this.converterClient = converterClient;
        this.fileUploaders = fileUploaders.stream()
                .filter(uploader -> uploader.getUploaderType().equals(OnlyofficeUploaderType.FILE))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param request
     * @throws OnlyofficeRunnerRuntimeException
     * @throws IOException
     */
    public void run(ConverterRequest request) throws OnlyofficeRunnerRuntimeException, IOException {
        ConverterResponse response = this.converterClient.convert(request);
        for (OnlyofficeUploader<ConverterRequest> uploader : fileUploaders) {
            doUpload(request, response, uploader);
        }
    }

    /**
     *
     * @param request
     * @param credentials
     * @throws OnlyofficeRunnerRuntimeException
     * @throws IOException
     */
    public void run(ConverterRequest request, Credentials credentials) throws OnlyofficeRunnerRuntimeException, IOException {
        ConverterResponse response = this.converterClient.convert(request, credentials);
        for (OnlyofficeUploader<ConverterRequest> uploader : fileUploaders) {
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
    private void doUpload(ConverterRequest request, ConverterResponse response, OnlyofficeUploader<ConverterRequest> uploader) throws IOException {
        try (InputStream inputStream = new URL(response.getFileUrl()).openStream()) {
            uploader.upload(request, inputStream);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
