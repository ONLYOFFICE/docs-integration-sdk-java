/**
 *
 * (c) Copyright Ascensio System SIA 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.onlyoffice.model.documenteditor.callback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the status of the document.
 */
@AllArgsConstructor
@Getter
public enum Status {
    /**
     * The document is being edited (the status ID is 1).
     */
    EDITING(1),

    /**
     * The document is ready for saving (the status ID is 2).
     */
    SAVE(2),

    /**
     * The document saving error has occurred (the status ID is 3).
     */
    SAVE_CORRUPTED(3),

    /**
     * The document is closed with no changes (the status ID is 4).
     */
    CLOSED(4),

    /**
     * The document is being edited, but the current document state is saved (the status ID is 6).
     */
    FORCESAVE(6),

    /**
     * The error has occurred while force saving the document (the status ID is 7).
     */
    FORCESAVE_CORRUPTED(7);

    /**
     * Defines the status ID.
     */
    private final int id;

    /**
     * Defines a map of the statuses with their IDs.
     */
    private static final Map<Integer, Status> BY_ID = new HashMap<>();

    static {
        for (Status e: values()) {
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
    public static Status valueOfId(final Integer code) {
        return BY_ID.get(code);
    }

    /**
     * Returns the status ID.
     *
     * @return The status ID.
     */
    @JsonValue
    int getId() {
        return this.id;
    }
}
