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

@AllArgsConstructor
@Getter
public enum ForcesaveType {
    COMMAND_SERVICE(0),
    SAVE_BUTTON(1),
    TIMER(2);

    private final int id;

    private static final Map<Integer, ForcesaveType> BY_ID = new HashMap<>();

    static {
        for (ForcesaveType e: values()) {
            BY_ID.put(e.getId(), e);
        }
    }

    @JsonCreator
    public static ForcesaveType valueOfId(final Integer code) {
        return BY_ID.get(code);
    }

    @JsonValue
    int getId() {
        return this.id;
    }
}
