package core.runner;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.OnlyofficePostProcessor;
import core.processor.OnlyofficePreProcessor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OnlyofficeCallbackRunnerBase implements OnlyofficeCallbackRunner {
    private final OnlyofficeCallbackProcessor callbackProcessor;
    private final List<OnlyofficePreProcessor<Callback>> callbackPreProcessors;
    private final List<OnlyofficePostProcessor<Callback>> callbackPostProcessors;

    /**
     *
     * @param callback
     * @throws RuntimeException
     */
    public void run(Callback callback) throws RuntimeException {
        callbackPreProcessors.forEach(preProcessor -> {
            preProcessor.processBefore();
            preProcessor.processBefore(callback);
        });

        this.callbackProcessor.handleCallback(callback);

        callbackPostProcessors.forEach(postProcessor -> {
            postProcessor.processAfter();
            postProcessor.processAfter(callback);
        });
    }
}
