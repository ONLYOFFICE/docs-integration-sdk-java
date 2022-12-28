package core.model.config.editor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Toolbar {
    TOP("top"),
    BOTTOM("bottom");

    private String position;

    Toolbar(String position) {
        this.position = position;
    }

    @JsonCreator
    public static Toolbar fromString(String key) {
        return key == null
                ? null
                : Toolbar.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getPosition() {
        return position;
    }
}
