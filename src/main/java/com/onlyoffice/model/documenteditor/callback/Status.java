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
public enum Status {

    EDITING(1),

    SAVE(2),

    SAVE_CORRUPTED(3),

    CLOSED(4),

    FORCESAVE(6),

    FORCESAVE_CORRUPTED(7);

    private final int id;

    private static final Map<Integer, Status> BY_ID = new HashMap<>();

    static {
        for (Status e: values()) {
            BY_ID.put(e.getId(), e);
        }
    }

    @JsonCreator
    public static Status valueOfId(final Integer code) {
        return BY_ID.get(code);
    }

    @JsonValue
    int getId() {
        return this.id;
    }
}
