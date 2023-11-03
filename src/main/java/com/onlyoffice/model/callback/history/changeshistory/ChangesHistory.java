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

package com.onlyoffice.model.callback.history.changeshistory;

import com.onlyoffice.model.callback.history.changeshistory.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines the changes from the history object returned after saving the document.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangesHistory {
    /**
     * Defines the document version creation date.
     */
    private String created;

    /**
     * Defines the user who is the author of the document version.
     */
    private User user;
}
