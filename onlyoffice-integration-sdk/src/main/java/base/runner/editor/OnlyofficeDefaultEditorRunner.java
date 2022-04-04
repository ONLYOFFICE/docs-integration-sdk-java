package base.runner.editor;

import core.model.config.Config;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.post.OnlyofficeEditorPostProcessor;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.runner.OnlyofficeEditorRunner;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class OnlyofficeDefaultEditorRunner implements OnlyofficeEditorRunner {
    private final OnlyofficeEditorProcessor editorProcessor;
    private final List<OnlyofficeEditorPreProcessor> editorPreProcessors;
    private final List<OnlyofficeEditorPostProcessor> editorPostProcessors;

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
        for (OnlyofficeEditorPreProcessor preProcessor : editorPreProcessors) {
            preProcessor.processBefore();
            preProcessor.processBefore(config);
        }

        this.editorProcessor.process(config);

        for (OnlyofficeEditorPostProcessor postProcessor : editorPostProcessors) {
            postProcessor.processAfter();
            postProcessor.processAfter(config);
        }
    }
}
