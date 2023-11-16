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

package com.onlyoffice.service.documenteditor.config;

import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.User;
import com.onlyoffice.model.documenteditor.Config;
import com.onlyoffice.model.documenteditor.config.Document;
import com.onlyoffice.model.documenteditor.config.document.Info;
import com.onlyoffice.model.documenteditor.config.document.Permissions;
import com.onlyoffice.model.documenteditor.config.document.ReferenceData;
import com.onlyoffice.model.documenteditor.config.document.Type;
import com.onlyoffice.model.documenteditor.config.EditorConfig;
import com.onlyoffice.model.documenteditor.config.editorconfig.CoEditing;
import com.onlyoffice.model.documenteditor.config.editorconfig.Customization;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Goback;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Review;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.review.ReviewDisplay;
import com.onlyoffice.model.documenteditor.config.editorconfig.Embedded;
import com.onlyoffice.model.documenteditor.config.editorconfig.Mode;
import com.onlyoffice.model.documenteditor.config.editorconfig.Plugins;
import com.onlyoffice.model.documenteditor.config.editorconfig.Recent;
import com.onlyoffice.model.documenteditor.config.editorconfig.Template;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.regex.Pattern;


@AllArgsConstructor
public class DefaultConfigService implements ConfigService {

    @Getter(AccessLevel.PROTECTED)
    private final DocumentManager documentManager;

    @Getter(AccessLevel.PROTECTED)
    private final UrlManager urlManager;

    @Getter(AccessLevel.PROTECTED)
    private final JwtManager jwtManager;

    @Getter(AccessLevel.PROTECTED)
    private final SettingsManager settingsManager;

    public Config createConfig(final String fileId, final Mode mode, final String userAgent) {
        return createConfig(fileId, mode, getType(userAgent));
    }

    public Config createConfig(final String fileId, final Mode mode, final Type type) {
        String documentName = documentManager.getDocumentName(fileId);

        Permissions permissions = getPermissions(fileId);

        Document document = Document.builder()
                .fileType(documentManager.getExtension(documentName))
                .key(documentManager.getDocumentKey(fileId, type.equals(Type.EMBEDDED)))
                .referenceData(getReferenceData(fileId))
                .title(documentName)
                .url(urlManager.getFileUrl(fileId))
                .info(getInfo(fileId))
                .permissions(permissions)
                .build();


        EditorConfig editorConfig = EditorConfig.builder()
                .coEditing(getCoEditing(null))
                .createUrl(urlManager.getCreateUrl(fileId))
                .mode(mode)
                .user(getUser())
                .recent(getRecent(null))
                .templates(getTemplates(fileId))
                .customization(getCustomization(fileId))
                .plugins(getPlugins(null))
                .build();

        if (permissions != null && permissions.getEdit() && mode.equals(Mode.EDIT)) {
            editorConfig.setCallbackUrl(urlManager.getCallbackUrl(fileId));
        }

        if (type.equals(Type.EMBEDDED)) {
            editorConfig.setEmbedded(getEmbedded(fileId));
        }

        Config config = Config.builder()
                .width("100%")
                .height("100%")
                .type(type)
                .documentType(documentManager.getDocumentType(documentName))
                .document(document)
                .editorConfig(editorConfig)
                .build();

        if (settingsManager.isSecurityEnabled()) {
            config.setToken(jwtManager.createToken(config));
        }

        return config;
    }

    public ReferenceData getReferenceData(final String fileId) {
        return null;
    }

    public Info getInfo(final String fileId) {
        return null;
    }

    public Permissions getPermissions(final String fileId) {
        return null;
    }

    public CoEditing getCoEditing(final Object object) {
        return null;
    }

    public List<Recent> getRecent(final Object object) {
        return null;
    }

    public List<Template> getTemplates(final String fileId) {
        return null;
    }

    public User getUser() {
        return null;
    }

    public Customization getCustomization(final String fileId) {
        Goback goback = Goback.builder().build();

        if (urlManager.getGobackUrl(fileId) != null) {
            goback.setUrl(urlManager.getGobackUrl(fileId));
        }

        Customization customization = Customization.builder()
                .chat(settingsManager.getSettingBoolean("editor.customization.chat", true))
                .compactHeader(settingsManager.getSettingBoolean("editor.customization.compactHeader", false))
                .feedback(settingsManager.getSettingBoolean("editor.customization.feedback", false))
                .forcesave(settingsManager.getSettingBoolean("editor.customization.forcesave", false))
                .goback(goback)
                .help(settingsManager.getSettingBoolean("editor.customization.help", true))
                .toolbarNoTabs(settingsManager.getSettingBoolean("editor.customization.toolbarNoTabs", false))
                .build();

        String reviewDisplay = settingsManager.getSetting("editor.customization.reviewDisplay");

        if (reviewDisplay == null || reviewDisplay.isEmpty()) {
            reviewDisplay = ReviewDisplay.ORIGINAL.name().toLowerCase();
        }

        if (!reviewDisplay.equals("original")) {
            Review review = Review.builder()
                    .reviewDisplay(ReviewDisplay.valueOf(reviewDisplay))
                    .build();

            customization.setReview(review);
        }

        return customization;
    }

    public Embedded getEmbedded(final String fileId) {
        return null;
    }

    public Plugins getPlugins(final Object object) {
        return null;
    }

    public Type getType(final String userAgent) {
        Pattern pattern = Pattern.compile(
                settingsManager.getSDKSetting("integration-sdk.mobile.user-agent"),
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
        );

        if (userAgent != null && pattern.matcher(userAgent).find()) {
            return Type.MOBILE;
        } else {
            return Type.DESKTOP;
        }
    }
}
