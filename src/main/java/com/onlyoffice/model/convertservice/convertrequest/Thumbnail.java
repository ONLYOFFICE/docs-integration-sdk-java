package com.onlyoffice.model.convertservice.convertrequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines the settings for the thumbnail when specifying the image formats ("bmp", "gif", "jpg", "png") as
 * {@link com.onlyoffice.model.convertservice.ConvertRequest#outputtype outputtype}.
 */
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
