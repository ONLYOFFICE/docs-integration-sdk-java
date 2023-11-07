package com.onlyoffice.model.documenteditor.config.document.info.sharingsettings;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the access rights for the user with the name above.
 */
public enum Permissions {
    /**
     * The user has the full access to the document.
     */
    @JsonProperty("Full Access")
    FULL_ACCESS,

    /**
     * The user has the read-only access to the document.
     */
    @JsonProperty("Read Only")
    READ_ONLY,

    /**
     * The user does not have the access to the document.
     */
    @JsonProperty("Deny Access")
    DENY_ACCESS
}
