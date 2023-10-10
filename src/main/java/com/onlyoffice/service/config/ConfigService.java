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
    Config createConfig(String fileId, Mode mode, Type type, String userAgent);
    Config createConfig(String fileId, Mode mode, Type type);

    ReferenceData getReferenceData(String fileId);

    Info getInfo(String fileId);

    Permissions getPermissions(String fileId);

    CoEditing getCoEditing(Object object);

    List<Recent> getRecent(Object object);

    List<Template> getTemplates(String fileId);

    User getUser();

    Customization getCustomization(String fileId);

    Embedded getEmbedded(String fileId);

    Plugins getPlugins(Object object);

    Type getType(String userAgent);
}
