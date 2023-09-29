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

package com.onlyoffice.model.config.editor.customization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.config.editor.customization.anonymous.Anonymous;
import com.onlyoffice.model.config.editor.customization.customer.Customer;
import com.onlyoffice.model.config.editor.customization.features.Features;
import com.onlyoffice.model.config.editor.customization.goback.Goback;
import com.onlyoffice.model.config.editor.customization.logo.Logo;
import com.onlyoffice.model.config.editor.customization.macrosmode.MacrosMode;
import com.onlyoffice.model.config.editor.customization.review.Review;
import com.onlyoffice.model.config.editor.customization.review.ReviewDisplay;
import com.onlyoffice.model.config.editor.customization.unit.Unit;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Customization {
    private Anonymous anonymous;
    private Boolean autosave;
    private Boolean chat;
    private Boolean commentAuthorOnly;
    private Boolean comments;
    private Boolean compactHeader;
    private Boolean compatibleFeatures;
    private Customer customer;
    private Features features;
    private Boolean feedback;
    private Boolean forcesave;
    private Goback goback;
    private Boolean help;
    private Boolean hideNotes;
    private Boolean hideRightMenu;
    private Boolean hideRulers;
    private String integrationMode;
    private Logo logo;
    private Boolean macros;
    private MacrosMode macrosMode;
    private Boolean mentionShare;
    private Boolean plugins;
    private Review review;
    @Deprecated
    private ReviewDisplay reviewDisplay;
    @Deprecated
    private Boolean showReviewChanges;
    @Deprecated
    private Boolean spellcheck;
    private Boolean toolbarHideFileName;
    private Boolean toolbarNoTabs;
    @Deprecated
    private Boolean trackChanges;
    private String uiTheme;
    private Unit unit;
    private Integer zoom;
}
