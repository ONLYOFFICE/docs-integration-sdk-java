package base.runner.editor;

import core.model.config.Config;
import core.processor.OnlyofficePreProcessor;
import core.processor.OnlyofficeProcessor;
import core.processor.OnlyofficePostProcessor;
import core.runner.OnlyofficeRunner;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class OnlyofficeDefaultEditorRunner implements OnlyofficeRunner<Config> {
    private final OnlyofficeProcessor<Config> editorProcessor;
    private final List<OnlyofficePreProcessor<Config>> editorPreProcessors;
    private final List<OnlyofficePostProcessor<Config>> editorPostProcessors;

    /**
     *
     * @param config
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    public void run(Config config) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        for (OnlyofficePreProcessor<Config> preProcessor : editorPreProcessors) {
            preProcessor.processBefore();
            preProcessor.processBefore(config);
        }

        this.editorProcessor.process(config);

        for (OnlyofficePostProcessor<Config> postProcessor : editorPostProcessors) {
            postProcessor.processAfter();
            postProcessor.processAfter(config);
        }
    }
}
