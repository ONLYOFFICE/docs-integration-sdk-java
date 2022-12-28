package core.model.config.editor.customization.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MacrosMode {
    DISABLE("disable"),
    ENABLE("enable"),
    WARN("warn");

    private String mode;

    MacrosMode(String mode) {
        this.mode = mode;
    }

    @JsonCreator
    public static MacrosMode fromString(String key) {
        return key == null
                ? null
                : MacrosMode.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getMode() {
        return mode;
    }
}
