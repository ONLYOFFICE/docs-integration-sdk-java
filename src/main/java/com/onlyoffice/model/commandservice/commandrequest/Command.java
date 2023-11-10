package com.onlyoffice.model.commandservice.commandrequest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the command type.
 */
public enum Command {
    /**
     * This command allows to disconnect the specified users from the document editing service.
     */
    @JsonProperty("drop")
    DROP,

    /**
     * This command allows to forcibly save the document being edited without closing it.
     */
    @JsonProperty("fercesave")
    FORCESAVE,

    /**
     * This command allows to request a document status and the list of the identifiers
     * of the users who opened the document for editing.
     */
    @JsonProperty("info")
    INFO,

    /**
     * This command allows to request the license from Document Server with information
     * about the server and user quota.
     */
    @JsonProperty("licence")
    LICENSE,

    /**
     * This command allows to update the meta information of the document
     * for all collaborative editors.
     */
    @JsonProperty("meta")
    META,

    /**
     * This command allows to request the current version number of Document Server.
     */
    @JsonProperty("version")
    VERSION
}
