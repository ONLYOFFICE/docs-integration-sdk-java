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
    protected LinkedHashMap<String, ImmutableMap<String, Object>> processors = new LinkedHashMap<>();

    public boolean hasProcessor(String processorName) {
        if (this.processors == null) return false;
        return this.processors.containsKey(processorName);
    }

    public void addProcessor(String processorName, ImmutableMap<String, Object> processorSchema) {
        if (this.processors == null) return;
        this.processors.putIfAbsent(processorName, processorSchema);
    }

    public ImmutableMap<String, Object> getProcessorSchema(String processorName) {
        if (this.processors == null) return null;
        return this.processors.getOrDefault(processorName, ImmutableMap.of());
    }
}
