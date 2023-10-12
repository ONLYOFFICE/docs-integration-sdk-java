package com.onlyoffice.model.documenteditor.config.document.info.sharingsettings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Permissions {
    @JsonProperty("Full Access")
    FULL_ACCESS,
    @JsonProperty("Read Only")
    READ_ONLY,
    @JsonProperty("Deny Access")
    DENY_ACCESS
}
