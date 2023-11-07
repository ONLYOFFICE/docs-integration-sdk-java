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

package com.onlyoffice.model.documenteditor.config.document.permissions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Defines the groups whose comments the user can edit, remove and/or view.
 * The [""] value means that the user can edit/remove/view comments made by someone who belongs to none of these groups
 * (for example, if the document is reviewed in third-party editors). If the value is [], the user cannot
 * edit/remove/view comments made by any group. If the "edit", "remove" and "view" parameters are "" or not specified,
 * then the user can view/edit/remove comments made by any user.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CommentGroup {
    /**
     * Defines a list of groups whose comments the user can edit.
     */
    private List<String> edit;

    /**
     * Defines a list of groups whose comments the user can remove.
     */
    private List<String> remove;

    /**
     * Defines a list of groups whose comments the user can view.
     */
    private List<String> view;
}
