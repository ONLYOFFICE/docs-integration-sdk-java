package core.runner.implementation;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackPostProcessor;
import core.processor.OnlyofficeCallbackPreProcessor;
import core.processor.OnlyofficeCallbackProcessor;
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
    public Callback run(Callback request) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        if (request == null)
            throw new OnlyofficeRunnerRuntimeException("Expected to get a Callback instance. Got null");

        this.preProcessors.forEach(processor -> processor.processBefore(request));

        this.callbackProcessor.process(request);

        this.postProcessors.forEach(processor -> processor.processAfter(request));

        return request;
    }
}
