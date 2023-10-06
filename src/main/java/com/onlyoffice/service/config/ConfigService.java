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

package com.onlyoffice.service.config;

import com.onlyoffice.model.config.Config;
import com.onlyoffice.model.config.document.info.Info;
import com.onlyoffice.model.config.document.permissions.Permissions;
import com.onlyoffice.model.config.document.referencedata.ReferenceData;
import com.onlyoffice.model.config.document.type.Type;
import com.onlyoffice.model.config.editor.coediting.CoEditing;
import com.onlyoffice.model.config.editor.customization.Customization;
import com.onlyoffice.model.config.editor.embedded.Embedded;
import com.onlyoffice.model.config.editor.mode.Mode;
import com.onlyoffice.model.config.editor.plugins.Plugins;
import com.onlyoffice.model.config.editor.recent.Recent;
import com.onlyoffice.model.config.editor.template.Template;
import com.onlyoffice.model.config.editor.user.User;

import java.util.List;

public interface ConfigService {

    /**
     * Create configuration for documents editor.
     *
     * @param fileId the ID of the file
     * @param mode the mode opening editor, see {@link Mode}
     * @param userAgent the userAgent from request, for determine type editor (DESKTOP or MOBILE)
     * @return the object {@link Config}
     */
    Config createConfig(String fileId, Mode mode, String userAgent);

    /**
     * Create configuration for documents editor.
     *
     * @param fileId the ID of the file
     * @param mode the mode opening editor, see {@link Mode}
     * @param type the type opening editor, see {@link Type}
     * @return the object {@link Config}
     */
    Config createConfig(String fileId, Mode mode, Type type);

    /**
     * Gets {@link ReferenceData} object.
     *
     * @param fileId the ID of the file
     * @return the object {@link ReferenceData}
     */
    ReferenceData getReferenceData(String fileId);

    /**
     * Gets {@link Info} object.
     *
     * @param fileId the ID of the file
     * @return the object {@link Info}
     */
    Info getInfo(String fileId);

    /**
     * Gets {@link Permissions} object.
     *
     * @param fileId the ID of the file
     * @return the object {@link Permissions}
     */
    Permissions getPermissions(String fileId);

    /**
     * Gets {@link CoEditing} object.
     *
     * @param object
     * @return the object {@link CoEditing}
     */
    CoEditing getCoEditing(Object object);

    /**
     * Gets list {@link Recent} objects.
     *
     * @param object
     * @return the List objects {@link Recent}
     */
    List<Recent> getRecent(Object object);

    /**
     * Gets list {@link Template} objects.
     *
     * @param fileId the ID of the file
     * @return the List objects {@link Template}
     */
    List<Template> getTemplates(String fileId);

    /**
     * Gets {@link User} object.
     *
     * @return the objects {@link User}
     */
    User getUser();

    /**
     * Gets {@link Customization} object.
     *
     * @param fileId the ID of the file
     * @return the objects {@link Customization}
     */
    Customization getCustomization(String fileId);

    /**
     * Gets {@link Embedded} object.
     *
     * @param fileId the ID of the file
     * @return the objects {@link Embedded}
     */
    Embedded getEmbedded(String fileId);

    /**
     * Gets {@link Plugins} object.
     *
     * @param object
     * @return the objects {@link Plugins}
     */
    Plugins getPlugins(Object object);

    /**
     * Gets {@link Type} object.
     *
     * @param userAgent the userAgent from request, for determine type editor (DESKTOP or MOBILE)
     * @return the objects {@link Type}
     */
    Type getType(String userAgent);
}
