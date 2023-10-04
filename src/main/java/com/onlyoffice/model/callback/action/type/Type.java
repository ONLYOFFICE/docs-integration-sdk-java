package com.onlyoffice.model.callback.action.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum Type {
    DISCONNECTED(0),
    CONNECTED(1),
    CLICK_FORCESAVE(2);

    int id;

    Type(int id) {
        this.id = id;
    }

    private static final Map<Integer, Type> BY_ID = new HashMap<>();

    static {
        for (Type e: values()) {
            BY_ID.put(e.getId(), e);
        }
    }

    @JsonCreator
    public static Type valueOfId(Integer code) {
        return BY_ID.get(code);
    }

    @JsonValue
    int getId() {
        return this.id;
    }
}
