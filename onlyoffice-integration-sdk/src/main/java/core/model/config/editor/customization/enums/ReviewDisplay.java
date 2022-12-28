package core.model.config.editor.customization.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReviewDisplay {
    MARKUP("markup"),
    SIMPLE("simple"),
    FINAL("final"),
    ORIGINAL("original");

    private String mode;

    ReviewDisplay(String mode) {
        this.mode = mode;
    }

    @JsonCreator
    public static ReviewDisplay fromString(String key) {
        return key == null
                ? null
                : ReviewDisplay.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getMode() {
        return mode;
    }
}
