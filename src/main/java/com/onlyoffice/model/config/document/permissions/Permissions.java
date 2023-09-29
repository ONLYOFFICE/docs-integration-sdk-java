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

package com.onlyoffice.model.config.document.permissions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.config.document.permissions.commentgrop.CommentGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Permissions {
    private Boolean chat;
    private Boolean comment;
    private List<CommentGroup> commentGroups;
    private Boolean copy;
    private Boolean deleteCommentAuthorOnly;
    private Boolean download;
    private Boolean edit;
    private Boolean editCommentAuthorOnly;
    private Boolean fillForms;
    private Boolean modifyContentControl;
    private Boolean modifyFilter;
    private Boolean print;
    private Boolean protect;
    private Boolean rename;
    private Boolean review;
    private List<String> reviewGroups;
    private List<String> userInfoGroups;
}
