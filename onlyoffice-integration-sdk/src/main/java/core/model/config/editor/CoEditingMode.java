package core.model.config.editor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CoEditingMode {
    FAST("fast"),
    STRICT("strict");

    private String mode;

    CoEditingMode(String mode) {
        this.mode = mode;
    }

    @JsonCreator
    public static CoEditingMode fromString(String key) {
        return key == null
                ? null
                : CoEditingMode.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getMode() {
        return mode;
    }
}
