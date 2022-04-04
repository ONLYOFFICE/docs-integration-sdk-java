package base.runner;


import core.client.OnlyofficeConverterClient;
import core.model.Credentials;
import core.model.converter.request.ConverterRequest;
import core.model.converter.response.ConverterResponse;
import core.runner.OnlyofficeConverterRunner;
import core.uploader.OnlyofficeConverterUploader;
import core.uploader.OnlyofficeUploaderType;
import exception.OnlyofficeRunnerRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class OnlyofficeDefaultConverterRunner implements OnlyofficeConverterRunner {
    private final List<OnlyofficeConverterUploader> fileUploaders;
    private final OnlyofficeConverterClient converterClient;

    /**
     *
     * @param fileUploaders
     * @param converterClient
     */
    public OnlyofficeDefaultConverterRunner(List<OnlyofficeConverterUploader> fileUploaders, OnlyofficeConverterClient converterClient) {
        this.converterClient = converterClient;
        this.fileUploaders = fileUploaders.stream()
                .filter(uploader -> uploader.getUploaderType().equals(OnlyofficeUploaderType.FILE))
                .collect(Collectors.toList());
    }

    public void run(ConverterRequest request) throws OnlyofficeRunnerRuntimeException, IOException {
        ConverterResponse response = this.converterClient.convert(request);
        for (OnlyofficeConverterUploader uploader : fileUploaders) {
            doUpload(request, response, uploader);
        }
    }

    public void run(ConverterRequest request, Credentials credentials) throws OnlyofficeRunnerRuntimeException, IOException {
        ConverterResponse response = this.converterClient.convert(request, credentials);
        for (OnlyofficeConverterUploader uploader : fileUploaders) {
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
    private void doUpload(ConverterRequest request, ConverterResponse response, OnlyofficeConverterUploader uploader) throws IOException {
        try (InputStream inputStream = new URL(response.getFileUrl()).openStream()) {
            uploader.upload(request, inputStream);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
