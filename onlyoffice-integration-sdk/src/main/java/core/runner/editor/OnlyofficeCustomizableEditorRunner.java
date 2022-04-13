package core.runner.editor;

import com.google.common.collect.ImmutableMap;
import core.model.config.Config;
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
    public Config run(ConfigRequest request) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {
        if (request == null)
            throw new OnlyofficeRunnerRuntimeException("Expected to get a ConfigRequest instance. Got null");

        HashMap<String, Integer> invocations = new HashMap<>();

        while(true) {
            for (Map.Entry<String, ImmutableMap<String, Object>> processor : request.preProcessors) {
                String processorName = processor.getKey();
                if (!preProcessors.containsKey(processorName)) {
                    request.removePreProcessor(processorName);
                    continue;
                }

                if (!invocations.containsKey(processorName))
                    invocations.put(processorName, 0);

                Integer nextInvocation = invocations.get(processorName) + 1;
                if (nextInvocation > maxTotalHops)
                    throw new OnlyofficeRunnerRuntimeException("Exceeded total preprocessor hops");

                invocations.put(processorName, nextInvocation);

                preProcessors.get(processorName).processBefore();
                preProcessors.get(processorName).processBefore(request);
                request.removePreProcessor(processorName);
            }
            if (request.preProcessors.size() == 0) break;
        }

        invocations.clear();
        request.preProcessors.clear();

        editorProcessor.process(request);

        while(request.postProcessors.size() > 0) {
            for (Map.Entry<String, ImmutableMap<String, Object>> processor : request.postProcessors) {
                String processorName = processor.getKey();
                if (!postProcessors.containsKey(processorName)) {
                    request.removePreProcessor(processorName);
                    continue;
                }

                if (!invocations.containsKey(processorName))
                    invocations.put(processorName, 0);

                Integer nextInvocation = invocations.get(processorName) + 1;
                if (nextInvocation > maxTotalHops)
                    throw new OnlyofficeRunnerRuntimeException("Exceeded total preprocessor hops");

                invocations.put(processorName, nextInvocation);

                postProcessors.get(processorName).processAfter();
                postProcessors.get(processorName).processAfter(request);
                request.removePostProcessor(processorName);
            }
            if (request.postProcessors.size() == 0) break;
        }

        request.postProcessors.clear();

        return request.getConfig();
    }
}
