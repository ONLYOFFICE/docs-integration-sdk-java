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

package com.onlyoffice.model.documenteditor.config.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.document.permissions.CommentGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Defines the permission for the document to be edited and downloaded or not.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Permissions {
    /**
     * Defines if the chat functionality is enabled in the document or not.
     * In case the chat permission is set to "true", the "Chat" menu button will be displayed.
     * The default value is "true".
     */
    private Boolean chat;

    /**
     * Defines if the document can be commented or not.
     * In case the commenting permission is set to "true", the document side bar will contain the "Comment" menu option.
     * The document commenting will only be available for the document editor if the {@link
     * com.onlyoffice.model.documenteditor.config.EditorConfig#mode mode} parameter is set to "edit". The default
     * value coincides with the value of the {@link Permissions#edit edit} parameter.
     */
    private Boolean comment;

    /**
     * Defines the {@link com.onlyoffice.model.documenteditor.config.EditorConfig#user groups} whose comments the
     * user can edit, remove and/or view. The [""] value means that the user can edit/remove/view comments made by
     * someone who belongs to none of these groups (for example, if the document is reviewed in third-party editors).
     * If the value is [], the user cannot edit/remove/view comments made by any group. If the "edit", "remove" and
     * "view" parameters are "" or not specified, then the user can view/edit/remove comments made by any user.
     */
    private List<CommentGroup> commentGroups;

    /**
     * Defines if the content can be copied to the clipboard or not.
     * In case the parameter is set to "false", pasting the content will be available within the current document editor
     * only.
     * The default value is "true".
     */
    private Boolean copy;

    /**
     * Defines if the user can delete only his/her comments. The default value is "false".
     */
    private Boolean deleteCommentAuthorOnly;

    /**
     * Defines if the document can be downloaded or only viewed or edited online.
     * In case the downloading permission is set to "false", the "Download as..." menu option will be absent from the
     * "File" menu.
     * The default value is "true".
     */
    private Boolean download;

    /**
     * Defines if the document can be edited or only viewed.
     * In case the editing permission is set to "true", the "File" menu will contain the "Edit Document" menu option.
     * Please note that if the editing permission is set to "false", the document will be opened in viewer
     * and you will not be able to switch it to the editor even if the
     * {@link com.onlyoffice.model.documenteditor.config.EditorConfig#mode mode} parameter is set to "edit".
     * The default value is "true".
     */
    private Boolean edit;

    /**
     * Defines if the user can edit only his/her comments. The default value is "false".
     */
    private Boolean editCommentAuthorOnly;

    /**
     * Defines if the forms can be filled. Filling in forms will only be available for the document editor
     * if the {@link com.onlyoffice.model.documenteditor.config.EditorConfig#mode mode} parameter is set to "edit".
     * The default value coincides with the value of the {@link Permissions#edit edit} or the {@link Permissions#review
     * review} parameter.
     */
    private Boolean fillForms;

    /**
     * Defines if the content control settings can be changed.
     * Content control modification will only be available for the document editor
     * if the {@link com.onlyoffice.model.documenteditor.config.EditorConfig#mode mode} parameter is set to "edit".
     * The default value is "true".
     */
    private Boolean modifyContentControl;

    /**
     * Defines if the filter can applied globally ("true") affecting all the other users, or locally ("false"), i.e.
     * for the current user only. Filter modification will only be available for the spreadsheet editor if the
     * {@link com.onlyoffice.model.documenteditor.config.EditorConfig#mode mode} parameter is set to "edit".
     * The default value is "true".
     */
    private Boolean modifyFilter;

    /**
     * Defines if the document can be printed or not.
     * In case the printing permission is set to "false", the "Print" menu option will be absent from the "File" menu.
     * The default value is "true".
     */
    private Boolean print;

    /**
     * Defines if the "Protection" tab on the toolbar and the "Protect" button in the left menu are displayed ("true")
     * or hidden ("false").
     * The default value is "true"
     */
    private Boolean protect;

    /**
     * Defines if the "Rename..." button is displayed when using the "onRequestRename" event.
     * The default value is "false". Deprecated since version 6.0, please add the "onRequestRename" field instead.
     *
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/config/events#onRequestRename">"onRequestRename"
     * event in API ONLYOFFICE</a>
     */
    @Deprecated
    private Boolean rename;

    /**
     * Defines if the document can be reviewed or not.
     * In case the reviewing permission is set to "true", the document status bar will contain the "Review" menu option.
     * The document review will only be available for the document editor if the {@link
     * com.onlyoffice.model.documenteditor.config.EditorConfig#mode mode} parameter is set to "edit".
     * The default value coincides with the value of the {@link Permissions#edit edit} parameter.
     */
    private Boolean review;

    /**
     * Defines the {@link com.onlyoffice.model.documenteditor.config.EditorConfig#user groups} whose changes the user
     * can accept/reject. The [""] value means that the user can review changes made by someone who belongs to none of
     * these groups (for example, if the document is reviewed in third-party editors).
     * If the value is [], the user cannot review changes made by any group.
     * If the value is "" or not specified, then the user can review changes made by any user.
     */
    private List<String> reviewGroups;

    /**
     * Defines the groups of users whose information is displayed in the editors:
     * 1) the usernames are displayed in the list of the editing users in the editor header,
     * 2) when typing text, the user cursors and tooltips with their names are displayed,
     * 3) when locking objects in the strict co-editing mode, the usernames are displayed.
     * The ["Group1", ""] means that the information about users from Group1 and users who don't belong to any group
     * is displayed. The [] means that no user information is displayed at all.
     * The "undefined" or "" values mean that the information about all users is displayed.
     */
    private List<String> userInfoGroups;
}
