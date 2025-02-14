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

package com.onlyoffice.model.convertservice.convertrequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Thumbnail {

    /**
     * Defines the mode to fit the image to the height and width specified. Supported values:
     * "0" - stretch file to fit height and width,
     * "1" - keep the aspect for the image,
     * "2" - in this case, the width and height settings are not used.
     *  Instead of that, metric sizes of the page are converted into pixels with 96dpi.
     * E.g., the A4 (210 x 297 mm) page will turn out to be a picture with the 794 x 1123 pix dimensions.
     * The default value is "2".
     */
    private Integer aspect;

    /**
     * Defines if the thumbnails should be generated for the first page only or for all the document pages.
     * If "false", the zip archive containing thumbnails for all the pages will be created. The default value is "true".
     */
    private boolean first;

    /**
     * Defines the thumbnail height in pixels. The default value is "100".
     */
    private Integer height;

    /**
     * Defines the thumbnail width in pixels. The default value is "100".
     */
    private Integer width;
}
