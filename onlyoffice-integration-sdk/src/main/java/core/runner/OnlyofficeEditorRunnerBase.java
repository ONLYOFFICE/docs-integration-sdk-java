package core.runner;

import core.model.config.Config;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.OnlyofficePostProcessor;
import core.processor.OnlyofficePreProcessor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OnlyofficeEditorRunnerBase implements OnlyofficeEditorRunner {
    private final OnlyofficeEditorProcessor editorProcessor;
    private final List<OnlyofficePreProcessor<Config>> editorPreProcessors;
    private final List<OnlyofficePostProcessor<Config>> editorPostProcessors;

    /**
     *
     * @param config
     * @return
     * @throws RuntimeException
     */
    public Config run(Config config) throws RuntimeException {
        for (OnlyofficePreProcessor<Config> preProcessor : editorPreProcessors) {
            preProcessor.processBefore();
            preProcessor.processBefore(config);
        }

        config = this.editorProcessor.processEditorConfig(config);

        for (OnlyofficePostProcessor<Config> postProcessor : editorPostProcessors) {
            postProcessor.processAfter();
            postProcessor.processAfter(config);
        }

        return config;
    }
}
