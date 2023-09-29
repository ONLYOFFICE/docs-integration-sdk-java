package com.onlyoffice.model.callback.action.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {
    DISCONNECTED(0),
    CONNECTED(1),
    CLICK_FORCESAVE(2);

    int id;

    Type(int id) {
        this.id = id;
    }

    @JsonValue
    int getId() {
        return this.id;
    }
}
