package core.runner.implementation;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.postprocessor.OnlyofficeCallbackPostProcessor;
import core.processor.preprocessor.OnlyofficeCallbackPreProcessor;
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
     * @return
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    public Callback run(CallbackRequest request) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        if (request == null)
            throw new OnlyofficeRunnerRuntimeException("Expected to get a CallbackRequest instance. Got null");

        preProcessors.forEach(processor -> {
            processor.run(request);
        });

        this.callbackProcessor.process(request.getCallback());

        postProcessors.forEach(processor -> {
            processor.run(request);
        });

        return request.getCallback();
    }
}
