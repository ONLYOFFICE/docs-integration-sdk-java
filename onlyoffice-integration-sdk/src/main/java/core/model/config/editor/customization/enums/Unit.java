package core.model.config.editor.customization.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Unit {
    CM("cm"),
    PT("pt"),
    INCH("inch");

    private String unit;

    Unit(String unit) {
        this.unit = unit;
    }

    @JsonCreator
    public static Unit fromString(String key) {
        return key == null
                ? null
                : Unit.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getUnit() {
        return unit;
    }
}
