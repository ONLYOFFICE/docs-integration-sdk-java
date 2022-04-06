package core.runner.editor;

import com.google.common.collect.ImmutableMap;
import core.model.config.Config;
import lombok.*;

import java.util.LinkedHashMap;

@Builder
@Getter
@Setter
@ToString
public class ConfigRequest {
    private Config config;
    @Builder.Default
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LinkedHashMap<String, ImmutableMap<String, Object>> preProcessors = new LinkedHashMap<>();
    @Builder.Default
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LinkedHashMap<String, ImmutableMap<String, Object>> postProcessors = new LinkedHashMap<>();

    public boolean hasPreProcessor(String preProcessorName) {
        if (this.preProcessors == null) return false;
        return this.preProcessors.containsKey(preProcessorName);
    }

    public boolean hasPostProcessor(String postProcessorName) {
        if (this.postProcessors == null) return false;
        return this.postProcessors.containsKey(postProcessorName);
    }

    public void addPreProcessor(String preProcessorName, ImmutableMap<String, Object> preProcessorSchema) {
        if (this.preProcessors == null) return;
        this.preProcessors.putIfAbsent(preProcessorName, preProcessorSchema);
    }

    public void addPostProcessor(String postProcessorName, ImmutableMap<String, Object> postProcessorSchema) {
        if (this.postProcessors == null) return;
        this.postProcessors.putIfAbsent(postProcessorName, postProcessorSchema);
    }

    public void replacePreProcessor(String preProcessorName, ImmutableMap<String, Object> preProcessorSchema) {
        if (this.preProcessors == null) return;
        this.preProcessors.replace(preProcessorName, preProcessorSchema);
    }

    public void replacePostProcessor(String postProcessorName, ImmutableMap<String, Object> postProcessorSchema) {
        if (this.postProcessors == null) return;
        this.postProcessors.replace(postProcessorName, postProcessorSchema);
    }

    public ImmutableMap<String, Object> getPreProcessorSchema(String preProcessorName) {
        if (this.preProcessors == null) return null;
        return this.preProcessors.getOrDefault(preProcessorName, null);
    }

    public ImmutableMap<String, Object> getPostProcessorSchema(String postProcessorName) {
        if (this.postProcessors == null) return null;
        return this.postProcessors.getOrDefault(postProcessorName, null);
    }
}
