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
import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeSequentialCallbackRunner implements OnlyofficeCallbackRunner {
    private final OnlyofficeCallbackProcessor callbackProcessor;
    private final Map<String, OnlyofficeCallbackPreProcessor> preProcessors;
    private final Map<String, OnlyofficeCallbackPostProcessor> postProcessors;

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
            throw new OnlyofficeRunnerRuntimeException("Expected to get a CallbackRequest instance. Got null");

        preProcessors.forEach((name, processor) -> {
            processor.processBefore();
            processor.processBefore(request);
        });

        this.callbackProcessor.process(request);

        postProcessors.forEach((name, processor) -> {
            processor.processAfter();
            processor.processAfter(request);
        });

        return request.getCallback();
    }
}
