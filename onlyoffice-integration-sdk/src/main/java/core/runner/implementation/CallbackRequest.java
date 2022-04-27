package core.runner.implementation;

import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import lombok.*;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
@ToString
public class CallbackRequest {
    @Builder.Default
    private Callback callback = Callback.builder().build();
    @Builder.Default
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    protected final ConcurrentLinkedQueue<Map.Entry<String, ImmutableMap<String, Object>>> preProcessors = new ConcurrentLinkedQueue<>();
    @Builder.Default
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    protected final ConcurrentLinkedQueue<Map.Entry<String, ImmutableMap<String, Object>>> postProcessors = new ConcurrentLinkedQueue<>();

    @Builder
    public CallbackRequest(Callback callback) {
        this.callback = callback;
    }

    public boolean hasPreProcessor(String preProcessorName) {
        return preProcessors.stream()
                .filter((entry) -> entry.getKey().equals(preProcessorName)).findFirst().isPresent();
    }

    public CallbackRequest addPreProcessor(String preProcessorName) {
        if (!hasPreProcessor(preProcessorName))
            preProcessors.add(new AbstractMap.SimpleEntry<>(preProcessorName, ImmutableMap.of()));
        return this;
    }

    public CallbackRequest addPreProcessor(String preProcessorName, ImmutableMap<String, Object> preProcessorSchema) {
        if (!hasPreProcessor(preProcessorName) && preProcessorSchema != null)
            preProcessors.add(new AbstractMap.SimpleEntry<>(preProcessorName, preProcessorSchema));
        return this;
    }

    public ImmutableMap<String, Object> getPreProcessorSchema(String preProcessorName) {
        Optional<Map.Entry<String, ImmutableMap<String, Object>>> optional = preProcessors.stream()
                .filter(entry -> entry.getKey().equals(preProcessorName)).findFirst();
        if (!optional.isPresent()) return null;
        return optional.get().getValue();
    }

    protected void removePreProcessor(String preProcessorName) {
        preProcessors.removeIf(entry -> entry.getKey().equals(preProcessorName));
    }

    public boolean hasPostProcessor(String postProcessorName) {
        return postProcessors.stream()
                .filter((entry) -> entry.getKey().equals(postProcessorName)).findFirst().isPresent();
    }

    public CallbackRequest addPostProcessor(String postProcessorName, ImmutableMap<String, Object> postProcessorSchema) {
        if (!hasPostProcessor(postProcessorName) && postProcessorSchema != null)
            postProcessors.add(new AbstractMap.SimpleEntry<>(postProcessorName, postProcessorSchema));
        return this;
    }

    public CallbackRequest addPostProcessor(String postProcessorName) {
        if (!hasPostProcessor(postProcessorName))
            postProcessors.add(new AbstractMap.SimpleEntry<>(postProcessorName, ImmutableMap.of()));
        return this;
    }

    public ImmutableMap<String, Object> getPostProcessorSchema(String postProcessorName) {
        Optional<Map.Entry<String, ImmutableMap<String, Object>>> optional = postProcessors.stream()
                .filter(entry -> entry.getKey().equals(postProcessorName)).findFirst();
        if (!optional.isPresent()) return null;
        return optional.get().getValue();
    }

    protected void removePostProcessor(String postProcessorName) {
        postProcessors.removeIf(entry -> entry.getKey().equals(postProcessorName));
    }
}
