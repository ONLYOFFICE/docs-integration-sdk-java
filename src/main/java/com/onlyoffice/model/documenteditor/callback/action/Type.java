package com.onlyoffice.model.documenteditor.callback.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the action type.
 */
@AllArgsConstructor
@Getter
public enum Type {
    /**
     * The user disconnects from the document co-editing (the type ID is 0).
     */
    DISCONNECTED(0),

    /**
     * A new user connects to the document co-editing (the type ID is 1).
     */
    CONNECTED(1),

    /**
     * The user clicks the forcesave button (the type ID is 2).
     */
    CLICK_FORCESAVE(2);

    /**
     * Defines the action type ID.
     */
    private final int id;

    /**
     * Defines a map of the action types with their IDs.
     */
    private static final Map<Integer, Type> BY_ID = new HashMap<>();

    static {
        for (Type e: values()) {
            BY_ID.put(e.getId(), e);
        }
    }

    /**
     * Returns the enum constant of this type with the specified ID.
     * The integer must match exactly an identifier used to declare an enum constant in this type.
     * 
     * @param code The ID of the enum constant to be returned.
     * @return The enum constant with the specified ID.
     */
    @JsonCreator
    public static Type valueOfId(final Integer code) {
        return BY_ID.get(code);
    }

    /**
     * Returns the action type ID.
     */
    @JsonValue
    int getId() {
        return this.id;
    }
}
