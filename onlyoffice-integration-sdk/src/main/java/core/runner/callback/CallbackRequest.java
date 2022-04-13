package core.runner.callback;

import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import lombok.*;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
@ToString
public class CallbackRequest {
    private Callback callback;
    @Builder.Default
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected final ConcurrentLinkedQueue<Map.Entry<String, ImmutableMap<String, Object>>> preProcessors = new ConcurrentLinkedQueue<>();
    @Builder.Default
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected final ConcurrentLinkedQueue<Map.Entry<String, ImmutableMap<String, Object>>> postProcessors = new ConcurrentLinkedQueue<>();

    @Builder
    public CallbackRequest(Callback callback) {
        this.callback = callback;
    }

    public boolean hasPreProcessor(String preProcessorName) {
        return this.preProcessors.stream()
                .filter((entry) -> entry.getKey().equals(preProcessorName)).findFirst().isPresent();
    }

    public boolean hasPostProcessor(String postProcessorName) {
        return this.postProcessors.stream()
                .filter((entry) -> entry.getKey().equals(postProcessorName)).findFirst().isPresent();
    }

    public CallbackRequest addPreProcessor(String preProcessorName, ImmutableMap<String, Object> preProcessorSchema) {
        if (!hasPreProcessor(preProcessorName) && preProcessorSchema != null)
            this.preProcessors.add(new AbstractMap.SimpleEntry<>(preProcessorName, preProcessorSchema));
        return this;
    }

    public CallbackRequest addPreProcessor(String preProcessorName) {
        if (!hasPreProcessor(preProcessorName))
            this.preProcessors.add(new AbstractMap.SimpleEntry<>(preProcessorName, ImmutableMap.of()));
        return this;
    }

    public CallbackRequest addPostProcessor(String postProcessorName, ImmutableMap<String, Object> postProcessorSchema) {
        if (!hasPostProcessor(postProcessorName) && postProcessorSchema != null)
            this.postProcessors.add(new AbstractMap.SimpleEntry<>(postProcessorName, postProcessorSchema));
        return this;
    }

    public CallbackRequest addPostProcessor(String postProcessorName) {
        if (!hasPostProcessor(postProcessorName))
            this.postProcessors.add(new AbstractMap.SimpleEntry<>(postProcessorName, ImmutableMap.of()));
        return this;
    }

    public ImmutableMap<String, Object> getPreProcessorSchema(String preProcessorName) {
        return this.preProcessors.stream().filter(entry -> entry.getKey().equals(preProcessorName))
                .findFirst().orElseGet(null).getValue();
    }

    public ImmutableMap<String, Object> getPostProcessorSchema(String postProcessorName) {
        return this.preProcessors.stream().filter(entry -> entry.getKey().equals(postProcessorName))
                .findFirst().orElseGet(null).getValue();
    }

    protected void removePreProcessor(String preProcessorName) {
        this.preProcessors.removeIf(entry -> entry.getKey().equals(preProcessorName));
    }

    protected void removePostProcessor(String postProcessorName) {
        this.postProcessors.removeIf(entry -> entry.getKey().equals(postProcessorName));
    }
}
