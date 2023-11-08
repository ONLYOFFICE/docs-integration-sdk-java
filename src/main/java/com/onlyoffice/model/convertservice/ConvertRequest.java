package com.onlyoffice.model.convertservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.convertservice.convertrequest.Thumbnail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines the request parameters that are sent to the "https://documentserver/ConvertService.ashx" address
 * where "documentserver" is the name of the server with the ONLYOFFICE Document Server installed.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvertRequest implements RequestEntity {
    /**
     * Defines the conversion request type: asynchronous or not.
     * When the asynchronous request type is used, the response is formed instantly.
     * In this case to get the result it is necessary to send requests without parameter change until the conversion
     * is finished.
     * The default value is "false".
     */
    private Boolean async;

    /**
     * Defines the file encoding when converting from "csv" or "txt" format. Main supported values:
     * "932" - Japanese (Shift-JIS), "950" - Chinese Traditional (Big5), "1250" - Central European (Windows),
     * "1251" - Cyrillic (Windows), "65001" - Unicode (UTF-8).
     *
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/server/blob/master/Common/sources/commondefines.js">
     *     All the supported values</a>
     */
    private Integer codePage;

    /**
     * Defines the delimiter characters for separating values when converting from "csv" format.
     * Supported values: "0" - no delimiter, "1" - tab, "2" - semicolon, "3" - colon, "4" - comma, "5" - space.
     */
    private Integer delimiter;

    /**
     * Defines the type of the document file to be converted.
     */
    private String filetype;

    /**
     * Defines the document identifier used to unambiguously identify the document file.
     */
    private String key;

    /**
     * Defines the resulting converted document type. Starting from version 7.0, file formats can be specified instead
     * of extensions. They are used when we do not know in advance what extension is required.
     * "ooxml" - defines that the file will be converted into "docx", "docm", "xlsx", "xlsm", "pptx" or "pptm".
     * For example, when the "doc" file is converted into the OOXML format, the resulting file can be "docx" or "docm"
     * if this file contains macros (the same for "xls" and "ppt").
     * It is also applied when converting XML files into OOXML formats ("docx", "xlsx" or "pptx" depending on the
     * content). "odf" - defines that the file will be converted into "odt", "ods" or "odp".
     * For example, it is used when converting XML files into ODF formats ("odt", "ods" or "odp" depending on the
     * content).
     */
    private String outputtype;

    /**
     * Defines the password for the document file if it is protected with a password.
     */
    private String password;

    /**
     * Defines the default display format for currency and date and time when converting from "Spreadsheet format" to
     * "pdf". Is set using the four letter ("en-US", "fr-FR", etc.) language codes. The default value is "en-US".
     */
    private String region;

    /**
     * Defines the settings for the thumbnail when specifying the image formats ("bmp", "gif", "jpg", "png") as
     * "outputtype".
     */
    private Thumbnail thumbnail;

    /**
     * Defines the converted file name.
     */
    private String title;

    /**
     * Defines the encrypted signature added to the Document Server config in the form of a token.
     */
    private String token;

    /**
     * Defines the absolute URL to the document to be converted.
     * Be sure to add a token when using local links. Otherwise, an error will occur.
     */
    private String url;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(final String tn) {
        this.token = tn;
    }
}
