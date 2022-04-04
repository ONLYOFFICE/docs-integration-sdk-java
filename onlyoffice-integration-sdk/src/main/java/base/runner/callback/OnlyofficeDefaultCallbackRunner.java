package base.runner.callback;

import core.model.callback.Callback;
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
public class OnlyofficeDefaultCallbackRunner implements OnlyofficeRunner<Callback> {
    private final OnlyofficeProcessor<Callback> callbackProcessor;
    private final List<OnlyofficePreProcessor<Callback>> callbackPreProcessors;
    private final List<OnlyofficePostProcessor<Callback>> callbackPostProcessors;

    /**
     *
     * @param callback
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    public void run(Callback callback) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        callbackPreProcessors.forEach(preProcessor -> {
            preProcessor.processBefore();
            preProcessor.processBefore(callback);
        });

        this.callbackProcessor.process(callback);

        callbackPostProcessors.forEach(postProcessor -> {
            postProcessor.processAfter();
            postProcessor.processAfter(callback);
        });
    }
}
