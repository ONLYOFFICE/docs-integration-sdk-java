package core.model.config.document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SharingSettingsPermission {
    FULL_ACCESS("Full Access"),
    READ_ONLY("Read Only"),
    DENY_ACCESS("Deny Access");

    private String permission;

    SharingSettingsPermission(String permission) {
        this.permission = permission;
    }

    @JsonCreator
    public static SharingSettingsPermission fromString(String key) {
        return key == null
                ? null
                : SharingSettingsPermission.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getPermission() {
        return permission;
    }
}
