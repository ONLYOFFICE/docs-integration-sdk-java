package core.runner.editor;

import core.processor.OnlyofficeEditorProcessor;
import core.processor.post.OnlyofficeEditorPostProcessor;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.runner.OnlyofficeEditorRunner;
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
public class OnlyofficeCustomizableEditorRunner implements OnlyofficeEditorRunner {
    private final OnlyofficeEditorProcessor editorProcessor;
    private final Map<String, OnlyofficeEditorPreProcessor> preProcessors;
    private final Map<String, OnlyofficeEditorPostProcessor> postProcessors;
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
    public void run(ConfigRequest request) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
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
        editorProcessor.process(request);

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

                if (request.preProcessors.size() == 0) break postLoop;
            }
        }
    }
}
