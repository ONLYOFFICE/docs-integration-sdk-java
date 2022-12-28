package core.runner.implementation;

import core.model.config.Config;
import core.processor.OnlyofficeEditorPostProcessor;
import core.processor.OnlyofficeEditorPreProcessor;
import core.processor.OnlyofficeEditorProcessor;
import core.runner.OnlyofficeEditorRunner;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class OnlyofficeSequentialEditorRunner implements OnlyofficeEditorRunner {
    private final OnlyofficeEditorProcessor editorProcessor;
    private final List<OnlyofficeEditorPreProcessor> preProcessors;
    private final List<OnlyofficeEditorPostProcessor> postProcessors;

    /**
     *
     * @param request
     * @return
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    public Config run(Config request) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        if (request == null)
            throw new OnlyofficeRunnerRuntimeException("Expected to get a Config instance. Got null");

        preProcessors.forEach(processor -> processor.processBefore(request));

        this.editorProcessor.process(request);

        postProcessors.forEach(processor -> processor.processAfter(request));

        return request;
    }
}
