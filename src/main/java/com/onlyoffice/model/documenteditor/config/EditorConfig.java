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

package com.onlyoffice.model.documenteditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.common.User;
import com.onlyoffice.model.documenteditor.config.editorconfig.CoEditing;
import com.onlyoffice.model.documenteditor.config.editorconfig.Customization;
import com.onlyoffice.model.documenteditor.config.editorconfig.Embedded;
import com.onlyoffice.model.documenteditor.config.editorconfig.Mode;
import com.onlyoffice.model.documenteditor.config.editorconfig.Plugins;
import com.onlyoffice.model.documenteditor.config.editorconfig.Recent;
import com.onlyoffice.model.documenteditor.config.editorconfig.Template;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Defines the editor interface parameters.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class EditorConfig {

    /**
     * Specifies the data received from the document editing service using the "onMakeActionLink" event
     * or the "onRequestSendNotify" event in "data.actionLink" parameter,
     * which contains the information about the action in the document that will be scrolled to.
     *
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/config/events#onMakeActionLink">onMakeActionLink
     * event in API ONLYOFFICE</a>
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/config/events#onRequestSendNotify">
     *     onRequestSendNotify event in API ONLYOFFICE</a>
     */
    private Object actionLink;

    /**
     * Specifies absolute URL to the document storage service
     * (which must be implemented by the software integrators who use ONLYOFFICE Document Server on their own server).
     */
    private String callbackUrl;

    /**
     * Defines the co-editing mode ("Fast" or "Strict") and the possibility to change it.
     * This parameter is used to apply the co-editing and viewing modes.
     */
    private CoEditing coEditing;

    /**
     * Defines the absolute URL of the document where it will be created and available after creation.
     * If not specified, there will be no creation button.
     * Instead of this field, you can use the "onRequestCreateNew" event.
     *
     * @see <a target="_top"href="https://api.onlyoffice.com/editors/config/events#onRequestCreateNew">
     * "onRequestCreateNew" event in API ONLYOFFICE</a>
     */
    private String createUrl;

    /**
     * Defines the editor interface language (if some other languages other than English are present).
     * Is set using the two letter (de, ru, it, etc.) language codes. The default value is "en".
     */
    private String lang;

    /**
     * Defines the default measurement units. Specify "us" or "ca" to set inches.
     * The default value is "".
     * Deprecated since version 8.2, please use the {@link #region} parameter instead.
     */
    @Deprecated
    private String location;

    /**
     * Defines the editor opening mode. Can be either "view" to open the document for viewing,
     * or "edit" to open the document in the editing mode allowing to apply changes to the document data.
     * The default value is "edit".
     */
    private Mode mode;

    /**
     * Defines the presence or absence of the documents in the "Open Recent..." menu option.
     */
    private List<Recent> recent;

    /**
     * Defines the default display format for currency and date and time (in the "Spreadsheet Editor" only).
     * Is set using the four letter (en-US, fr-FR, etc.) language codes.
     * For the default value the "lang" parameter is taken, or, if no regional setting corresponding to the "lang"
     * value is available, "en-US" is used.
     */
    private String region;

    /**
     * Defines the presence or absence of the templates in the "Create New..." menu option.
     */
    private List<Template> templates;

    /**
     * Defines the user currently viewing or editing the document.
     */
    private User user;

    /**
     * Defines the parameters to customize the editor interface so that it looked like your other products (if
     * there are any) and change the presence or absence of the additional buttons, links, change logos and editor owner
     * details.
     */
    private Customization customization;

    /**
     * Defines the parameters for the embedded document type only (see the
     * {@link com.onlyoffice.model.documenteditor.Config} section to find out how to define the embedded document type).
     * It allows to change the settings which define the behavior of the buttons in the embedded mode.
     */
    private Embedded embedded;

    /**
     * Defines the parameters to connect the special add-ons to your Document Server installation
     * which will help you add additional features to document editors.
     */
    private Plugins plugins;

}
