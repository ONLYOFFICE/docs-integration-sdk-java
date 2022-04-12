package core.runner.callback;

import core.processor.OnlyofficeCallbackProcessor;
import core.processor.post.OnlyofficeCallbackPostProcessor;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.runner.OnlyofficeCallbackRunner;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class OnlyofficeCustomizableCallbackRunner implements OnlyofficeCallbackRunner {
    private final OnlyofficeCallbackProcessor callbackProcessor;
    private final Map<String, OnlyofficeCallbackPreProcessor> preProcessors;
    private final Map<String, OnlyofficeCallbackPostProcessor> postProcessors;
    @Setter
    private int maxTotalHops = 3;

    /**
     *
     * @param request
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    public void run(CallbackRequest request) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        HashMap<String, Integer> invocations = new HashMap<>();

        preLoop: while(preProcessors.size() > 0) {
            for (String processorName : request.preProcessors.keySet()) {
                if (!preProcessors.containsKey(processorName)) {
                    request.preProcessors.remove(processorName);
                    continue;
                }

                if (!invocations.containsKey(processorName)) {
                    invocations.put(processorName, 0);
                }

                Integer nextInvocation = invocations.get(processorName) + 1;
                if (nextInvocation > maxTotalHops)
                    throw new OnlyofficeRunnerRuntimeException("Exceeded total preprocessor hops");
                invocations.put(processorName, nextInvocation);

                preProcessors.get(processorName).processBefore();
                preProcessors.get(processorName).processBefore(request);
                request.preProcessors.remove(processorName);

                if (request.preProcessors.size() == 0) break preLoop;
            }
        }

        invocations.clear();
        callbackProcessor.process(request);

        postLoop: while(postProcessors.size() > 0) {
            for (String processorName : request.postProcessors.keySet()) {
                if (!postProcessors.containsKey(processorName)) {
                    request.postProcessors.remove(processorName);
                    continue;
                }

                if (!invocations.containsKey(processorName)) {
                    invocations.put(processorName, 0);
                }

                Integer nextInvocation = invocations.get(processorName) + 1;
                if (nextInvocation > maxTotalHops)
                    throw new OnlyofficeRunnerRuntimeException("Exceeded total postprocessor hops");
                invocations.put(processorName, nextInvocation);

                postProcessors.get(processorName).processAfter();
                postProcessors.get(processorName).processAfter(request);
                request.postProcessors.remove(processorName);

                if (request.postProcessors.size() == 0) break postLoop;
            }
        }
    }
}
