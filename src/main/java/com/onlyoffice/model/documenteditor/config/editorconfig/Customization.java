/**
 *
 * (c) Copyright Ascensio System SIA 2025
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

package com.onlyoffice.model.documenteditor.config.editorconfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Anonymous;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Close;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Customer;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Features;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Goback;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Logo;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.MacrosMode;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Mobile;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Review;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.review.ReviewDisplay;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Defines the parameters to customize the editor interface so that it looked like your other products (if there are
 * any) and change the presence or absence of the additional buttons, links, change logos and editor owner details.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customization {

    /**
     * Adds a request for the anonymous name.
     */
    private Anonymous anonymous;

    /**
     * Defines if the "Autosave" menu option is enabled or disabled.
     * If set to "false", only "Strict" co-editing mode can be selected, as "Fast" does not work without autosave.
     * The default value is "true".
     */
    private Boolean autosave;

    /**
     * Defines if the "Chat" menu button is displayed or hidden.
     * Please note that in case you hide the "Chat" button, the corresponding chat functionality will also be disabled.
     * The default value is "true". Deprecated since version 7.1,
     * please use the
     * {@link com.onlyoffice.model.documenteditor.config.document.Permissions#chat document.permissions.chat} parameter
     * instead.
     */
    @Deprecated
    private Boolean chat;

    /**
     * Defines settings for the cross button to close the editor.
     */
    private Close close;

    /**
     * Defines if the user can edit and delete only his comments. The default value is "false".
     * Deprecated since version 6.3, please use the
     * {@link com.onlyoffice.model.documenteditor.config.document.Permissions#editCommentAuthorOnly
     * document.permissions.editCommentAuthorOnly} and
     * {@link com.onlyoffice.model.documenteditor.config.document.Permissions#deleteCommentAuthorOnly
     * document.permissions.deleteCommentAuthorOnly} fields instead.
     */
    @Deprecated
    private Boolean commentAuthorOnly;

    /**
     * Defines if the "Comments" menu button is displayed or hidden.
     * Please note that in case you hide the "Comments" button,
     * the corresponding commenting functionality will be available for viewing only, adding and editing comments will
     * be unavailable. The default value is "true".
     */
    private Boolean comments;

    /**
     * Defines if the additional action buttons are displayed in the upper part of the editor window header
     * next to the logo ("false") or in the toolbar ("true") making the header more compact.
     * The default value is "false".
     */
    private Boolean compactHeader;

    /**
     * Defines if the top toolbar type displayed is full (false) or compact (true). The default value is false.
     * Starting from version 8.3, this setting is also available for the viewer.
     * The default value for the view mode is true.
     */
    private Boolean compactToolbar;

    /**
     * Defines the use of functionality only compatible with the OOXML format.
     * For example, do not use comments on the entire document. The default value is "false".
     */
    private Boolean compatibleFeatures;

    /**
     * Contains the information which will be displayed in the editor "About" section and visible to all the editor
     * users.
     */
    private Customer customer;

    /**
     * Defines the parameters that the user can disable or customize if possible.
     */
    private Features features;

    /**
     * Defines if the "Feedback &amp; Support" menu button is displayed or hidden.
     */
    private Boolean feedback;

    /**
     * Adds the request for the file force saving to the {@link com.onlyoffice.model.documenteditor.Callback callback
     * handler} when saving the document within the document editing service (e.g. clicking the "Save" button, etc.).
     * The default value is "false".
     */
    private Boolean forcesave;

    /**
     * Defines settings for the "Open file location" menu button and upper right corner button.
     */
    private Goback goback;

    /**
     * Defines if the "Help" menu button is displayed or hidden. The default value is "true".
     */
    private Boolean help;

    /**
     * Defines if the note panel is displayed or hidden on first loading.
     * The default value is "false". This parameter is available for the presentation editor only.
     */
    private Boolean hideNotes;

    /**
     * Defines if the right menu is displayed or hidden on first loading. The default value is "false".
     */
    private Boolean hideRightMenu;

    /**
     * Defines if the editor rulers are displayed or hidden. This parameter is available for the document and
     * presentation editors. The default value is "false" for the document editor and "true" for presentations.
     */
    private Boolean hideRulers;

    /**
     * Defines the mode of embedding editors into the web page.
     * The "embed" value disables scrolling to the editor frame when it is loaded as the focus is not captured.
     */
    private String integrationMode;

    /**
     * Changes the image file at the top left corner of the editor header.
     * The recommended image height is 20 pixels.
     */
    private Logo logo;

    /**
     * Defines if document macros will be automatically run when the editor opens.
     * The default value is "true". The "false" value hides the macros settings from the user.
     */
    private Boolean macros;

    /**
     * Defines the macros run mode when autostart is enabled. The default value is "warn".
     */
    private MacrosMode macrosMode;

    /**
     * Defines the hint that describes the event after mentions in a comment.
     * If "true", a hint indicates that the user will receive a notification and access to the document.
     * If "false", a hint indicates that the user will receive only a notification of the mention. The default value
     * is "true".
     */
    private Boolean mentionShare;

    /**
     * Defines the mobile document editor settings.
     */
    private Mobile mobile;

    /**
     * Defines if the mobile document editor is opened in the view/edit mode on launch. The default value is "true".
     * Deprecated since version 8.2. Please use the {@link Mobile mobile.forceView} parameter instead.
     */
    @Deprecated
    private Boolean mobileForceView;

    /**
     * Defines if plugins will be launched and available. The default value is "true".
     */
    private Boolean plugins;

    /**
     * Defines the pointer mode (select or hand) when the presentation editor is loaded in the viewer.
     * The default value is select.
     */
    private String pointerMode;

    /**
     * Contains the information about the review mode.
     */
    private Review review;

    /**
     * Defines the review editing mode which will be used when the document is opened for viewing.
     * It will only be available for the document editor if {@link Mode} is set to "view".
     * The default value is "original" for viewer and "markup" for editor.
     * Deprecated since version 7.0. Please use the {@link Review review.reviewDisplay} parameter instead.
     */
    @Deprecated
    private ReviewDisplay reviewDisplay;

    /**
     * Starting from version 8.3, defines if the horizontal scroll is automatically displayed or hidden when the
     * spreadsheet editor is loaded. The default value is true.
     */
    private Boolean showHorizontalScroll;

    /**
     * Defines if the review changes panel is automatically displayed or hidden when the editor is loaded.
     * The default value is "false". Deprecated since version 7.0. Please use the
     * {@link Review review.showReviewChanges} parameter instead.
     */
    @Deprecated
    private Boolean showReviewChanges;

    /**
     * Starting from version 8.3, defines if the vertical scroll is automatically displayed or hidden when the
     * spreadsheet editor is loaded. The default value is true.
     */
    private Boolean showVerticalScroll;

    /**
     * Starting from version 8.3, defines the background color for the slide show in the presentation editor.
     * Can be represented in the HEX, RGB, or RGBA formats. For example, #ff0000, rgb(255, 0, 0), rgba(255, 0, 0, 0.5).
     */
    private String slidePlayerBackground;

    /**
     * Defines if the spell checker is automatically switched on or off when the editor is loaded.
     * Spell checker will only be available for the document editor and the presentation editor.
     * The default value is "true". Deprecated since version 7.1. Please use the {@link Features features.spellcheck}
     * parameter instead.
     */
    @Deprecated
    private Boolean spellcheck;

    /**
     * Starting from version 8.3, defines the Complete &amp; Submit button settings. If this parameter is a boolean
     * value, then it specifies whether the Complete &amp; Submit button will be displayed or hidden on the top toolbar.
     * Button will only be available for the pdf format. The default value is true.
     *
     * @see com.onlyoffice.model.documenteditor.config.editorconfig.customization.SubmitForm
     */
    private Object submitForm;

    /**
     * Defines if the document title is visible on the top toolbar ("false") or hidden ("true").
     * The default value is "false".
     */
    private Boolean toolbarHideFileName;

    /**
     * Defines if the top toolbar tabs are distinctly displayed ("false") or only highlighted to see which one is
     * selected ("true"). The default value is "false".
     * Deprecated since version 8.2, please use the {@link Features#tabStyle} parameter which is set
     * to line and the {@link Features#tabBackground} parameter which is equal to toolbar.
     */
    @Deprecated
    private Boolean toolbarNoTabs;

    /**
     * Defines if the document is opened in the review editing mode ("true") or not ("false")
     * regardless of the {@link com.onlyoffice.model.documenteditor.config.document.Permissions#review
     * document.permissions.review} parameter (the review mode is changed only for the current user). If the parameter
     * is undefined, the "document.permissions.review" value is used (for all the document users). Deprecated since
     * version 7.0. Please use the {@link Review review.trackChanges}
     * parameter instead.
     */
    @Deprecated
    private Boolean trackChanges;

    /**
     * Defines the editor theme settings. It can be set in two ways:
     * "theme id" - the user sets the theme parameter by its id ("theme-light", "theme-classic-light", "theme-dark",
     * "theme-contrast-dark"), "default theme" - the default dark or light theme value will be set ("default-dark",
     * "default-light"). The default light theme is "theme-classic-light". The first option has higher priority.
     */
    private String uiTheme;

    /**
     * Defines the measurement units used on the ruler and in dialog boxes.
     * The default value is centimeters ("cm").
     */
    private Unit unit;

    /**
     * Starting from version 8.3, defines the HEX color for the default heading styles in the document editor.
     */
    private String wordHeadingsColor;

    /**
     * Defines the document display zoom value measured in percent. Can take values larger than "0".
     * For text documents and presentations it is possible to set this parameter to "-1" (fitting the document to page
     * option) or to "-2" (fitting the document page width to the editor page). The default value is "100".
     */
    private Integer zoom;
}
