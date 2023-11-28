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
 * Defines the type of initiator when the force saving request is performed.
 */
@AllArgsConstructor
@Getter
public enum ForcesaveType {

    /**
     * The force saving request is performed to the command service (the type ID is 0).
     */
    COMMAND_SERVICE(0),

    /**
     * The force saving request is performed each time the saving is done (e.g. the "Save" button is clicked),
     * which is only available when the forcesave option is set to "true" (the type ID is 1).
     */
    SAVE_BUTTON(1),

    /**
     * The force saving request is performed by timer with the settings from the server config (the type ID is 2).
     */
    TIMER(2);

    /**
     * Defines the ID of the force saving request type.
     */
    private final int id;

    /**
     * Defines a map of the force saving request types with their IDs.
     */
    private static final Map<Integer, ForcesaveType> BY_ID = new HashMap<>();

    static {
        for (ForcesaveType e: values()) {
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
    public static ForcesaveType valueOfId(final Integer code) {
        return BY_ID.get(code);
    }

    /**
     * Returns the ID of the force saving request type.
     *
     * @return The ID of the force saving request type.
     */
    @JsonValue
    int getId() {
        return this.id;
    }
}
