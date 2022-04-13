package core.runner.callback;

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
public class OnlyofficeSequentialCallbackRunner implements OnlyofficeCallbackRunner {
    private final OnlyofficeCallbackProcessor callbackProcessor;
    private final List<OnlyofficeCallbackPreProcessor> preProcessors;
    private final List<OnlyofficeCallbackPostProcessor> postProcessors;

    /**
     *
     * @param request
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    public Callback run(CallbackRequest request) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        if (request == null)
            throw new OnlyofficeRunnerRuntimeException("Callback request is null");

        preProcessors.forEach(preProcessor -> {
            preProcessor.processBefore();
            preProcessor.processBefore(request);
        });

        this.callbackProcessor.process(request);

        postProcessors.forEach(postProcessor -> {
            postProcessor.processAfter();
            postProcessor.processAfter(request);
        });

        return request.getCallback();
    }
}
