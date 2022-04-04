package base.runner.callback;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.post.OnlyofficeCallbackPostProcessor;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.runner.OnlyofficeCallbackRunner;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class OnlyofficeDefaultCallbackRunner implements OnlyofficeCallbackRunner {
    private final OnlyofficeCallbackProcessor callbackProcessor;
    private final List<OnlyofficeCallbackPreProcessor> callbackPreProcessors;
    private final List<OnlyofficeCallbackPostProcessor> callbackPostProcessors;

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
