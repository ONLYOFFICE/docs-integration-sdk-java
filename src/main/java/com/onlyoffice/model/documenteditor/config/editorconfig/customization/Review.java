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

package com.onlyoffice.model.documenteditor.config.editorconfig.customization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.review.ReviewDisplay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains the information about the review mode.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    /**
     * Defines if the "Display mode" button is displayed or hidden on the "Collaboration" tab.
     * The default value is "false".
     */
    private Boolean hideReviewDisplay;

    /**
     * Defines the review display mode: show reviews in tooltips by hovering the changes ("true")
     * or in balloons by clicking the changes ("false"). The default value is "false".
     */
    private Boolean hoverMode;

    /**
     * Defines the review editing mode which will be used when the document is opened for viewing.
     * It will only be available for the document editor if
     * {@link com.onlyoffice.model.documenteditor.config.editorconfig.Mode} is set to "view". The default value is
     * "original".
     */
    private ReviewDisplay reviewDisplay;

    /**
     * Defines if the review changes panel is automatically displayed or hidden when the editor is loaded.
     * The default value is "false".
     */
    private Boolean showReviewChanges;

    /**
     * Defines if the document is opened in the review editing mode ("true") or not ("false")
     * regardless of the {@link com.onlyoffice.model.documenteditor.config.document.Permissions#review
     * document.permissions.review} parameter (the review mode is changed only for the current user). If the parameter
     * is undefined, the "document.permissions.review" value is used (for all the document users).
     */
    private Boolean trackChanges;
}
