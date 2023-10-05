package com.onlyoffice.model.callback.action.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum Type {
    DISCONNECTED(0),
    CONNECTED(1),
    CLICK_FORCESAVE(2);

    private final int id;

    private static final Map<Integer, Type> BY_ID = new HashMap<>();

    static {
        for (Type e: values()) {
            BY_ID.put(e.getId(), e);
        }
    }

    @JsonCreator
    public static Type valueOfId(final Integer code) {
        return BY_ID.get(code);
    }

    @JsonValue
    int getId() {
        return this.id;
    }
}
