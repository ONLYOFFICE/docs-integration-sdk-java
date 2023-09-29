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

package com.onlyoffice.model.config.editor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.config.editor.coediting.CoEditing;
import com.onlyoffice.model.config.editor.customization.Customization;
import com.onlyoffice.model.config.editor.embedded.Embedded;
import com.onlyoffice.model.config.editor.mode.Mode;
import com.onlyoffice.model.config.editor.plugins.Plugins;
import com.onlyoffice.model.config.editor.recent.Recent;
import com.onlyoffice.model.config.editor.template.Template;
import com.onlyoffice.model.config.editor.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class EditorConfig {
    private JSONObject actionLink;
    private String callbackUrl;
    private CoEditing coEditing;
    private String createUrl;
    private String lang;
    private String location;
    private Mode mode;
    private List<Recent> recent;
    private String region;
    private List<Template> templates;
    private User user;
    private Customization customization;
    private Embedded embedded;
    private Plugins plugins;

}
