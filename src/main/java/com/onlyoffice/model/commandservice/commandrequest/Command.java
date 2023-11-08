package com.onlyoffice.model.commandservice.commandrequest;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Command {
    @JsonProperty("drop")
    DROP,
    @JsonProperty("fercesave")
    FORCESAVE,
    @JsonProperty("info")
    INFO,
    @JsonProperty("licence")
    LICENSE,
    @JsonProperty("meta")
    META,
    @JsonProperty("version")
    VERSION
}
