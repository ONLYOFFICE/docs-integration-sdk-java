package com.onlyoffice.model.config.document.info.saringsettings.permissions;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Permissions {
    @JsonProperty("Full Access")
    FULL_ACCESS,
    @JsonProperty("Read Only")
    READ_ONLY,
    @JsonProperty("Deny Access")
    DENY_ACCESS
}
