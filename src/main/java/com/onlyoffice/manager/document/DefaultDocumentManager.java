/**
 *
 * (c) Copyright Ascensio System SIA 2024
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

package com.onlyoffice.manager.document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.config.document.DocumentType;
import com.onlyoffice.model.common.Format;
import com.onlyoffice.model.settings.SettingsConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@AllArgsConstructor
public abstract class DefaultDocumentManager implements DocumentManager {
    private static final String DOCS_FORMATS_JSON_PATH = "assets/document-formats/onlyoffice-docs-formats.json";

    /**
     * Defines the default maximum file size, used if the "integration-sdk.data.filesize.editing.max"
     * or "integration-sdk.data.filesize.conversion.max" properties are not specified in "settings.properties".
     */
    private static final int DEFAULT_MAX_FILE_SIZE = 5242880;

    /** {@link SettingsManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private SettingsManager settingsManager;

    /** Defines a list containing data about supported formats. */
    private static List<Format> formats;

    static {
        init();
    }

    @Override
    public List<Format> getFormats() {
        return this.formats;
    }

    @Override
    public abstract String getDocumentKey(String fileId, boolean embedded);

    @Override
    public abstract String getDocumentName(String fileId);

    @Override
    public String getExtension(final String fileName) {
        if (fileName == null) {
            return null;
        }

        if (!fileName.contains("."))  {
            return null;
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (extension.isEmpty()) {
            return null;
        }

        return extension.toLowerCase();
    }

    @Override
    public String getBaseName(final String fileName) {
        if (fileName == null) {
            return null;
        }

        if (!fileName.contains("."))  {
            return fileName;
        }

        String baseName = fileName.substring(0, fileName.lastIndexOf("."));

        if (baseName.isEmpty()) {
            return null;
        }

        return baseName;
    }

    @Override
    public DocumentType getDocumentType(final String fileName) {
        String fileExtension = getExtension(fileName);

        for (Format format : getFormats()) {
            if (format.getName().equals(fileExtension)) {
                return format.getType();
            }
        }

        return null;
    }

    @Override
    public boolean isEditable(final String fileName) {
        String extension = getExtension(fileName);

        if (extension == null) {
            return false;
        }

        Boolean lossyEditable = getLossyEditableMap().get(extension);

        if (lossyEditable != null) {
            return hasAction(fileName, "edit") || lossyEditable;
        }

        return hasAction(fileName, "edit");
    }

    @Override
    public boolean isViewable(final String fileName) {
        return hasAction(fileName, "view");
    }

    @Override
    public boolean isFillable(final String fileName) {
        return hasAction(fileName, "fill");
    }

    @Override
    public boolean hasAction(final String fileName, final String action) {
        String fileExtension = getExtension(fileName);

        if (fileExtension == null) {
            return false;
        }

        for (Format format : getFormats()) {
            if (format.getName().equals(fileExtension) && format.getActions().contains(action)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public InputStream getNewBlankFile(final String extension, final Locale locale) {
        String pathTemplate = "assets/document-templates/{0}/new.{1}";

        String path = MessageFormat.format(pathTemplate, locale.toLanguageTag(), extension);

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            path = MessageFormat.format(pathTemplate, locale.getLanguage(), extension);
            inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        }

        if (inputStream == null) {
            path = MessageFormat.format(pathTemplate, "default", extension);
            inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        }

        return inputStream;
    }

    @Override
    public String getDefaultExtension(final DocumentType documentType) {
        if (documentType == null) {
            return null;
        }

        switch (documentType) {
            case WORD:
                return "docx";
            case CELL:
                return "xlsx";
            case SLIDE:
                return "pptx";
            case PDF:
                return "pdf";
            default:
                return null;
        }
    }

    @Override
    public String getDefaultConvertExtension(final String fileName) {
        String extension = getExtension(fileName);

        if (extension == null) {
            return null;
        }

        for (Format format : getFormats()) {
            if (format.getType() != null && format.getName().equals(extension)) {
                switch (format.getType()) {
                    case WORD:
                        if (format.getConvert().contains("docx")) {
                            return "docx";
                        }
                        break;
                    case CELL:
                        if (format.getConvert().contains("xlsx")) {
                            return "xlsx";
                        }
                        break;
                    case SLIDE:
                        if (format.getConvert().contains("pptx")) {
                            return "pptx";
                        }
                        break;
                    case PDF:
                        if (format.getConvert().contains("pdf")) {
                            return "pdf";
                        }
                    default:
                        break;
                }
            }
        }

        return null;
    }

    @Override
    public List<String> getConvertExtensionList(final String fileName) {
        String extension = getExtension(fileName);

        if (extension == null) {
            return null;
        }

        for (Format format : getFormats()) {
            if (format.getName().equals(extension)) {
                return format.getConvert();
            }
        }

        return null;
    }

    @Override
    public Map<String, Boolean> getLossyEditableMap() {
        Map<String, Boolean> result = new HashMap<>();
        List<String> formatsLossyEditList = new ArrayList<>();

        String formatsLossyEdit = settingsManager.getSetting(SettingsConstants.LOSSY_EDIT);

        if (formatsLossyEdit != null && !formatsLossyEdit.isEmpty()) {
            formatsLossyEditList = Arrays.asList(
                    formatsLossyEdit
                            .replace("[", "")
                            .replace("]", "")
                            .split(", ")
            );
        }

        for (Format format : getFormats()) {
            if (format.getActions().contains("lossy-edit")) {
                result.put(format.getName(), formatsLossyEditList.contains(format.getName()));
            }
        }

        return result;
    }

    @Override
    public List<String> getInsertImageExtensions() {
        return settingsManager.getDocsIntegrationSdkProperties()
                .getDocumentServer()
                .getEditingService()
                .getInsertImage();
    }

    @Override
    public List<String> getCompareFileExtensions() {
        List<String> result = new ArrayList<>();

        for (Format format : getFormats()) {
            if (DocumentType.WORD.equals(format.getType())) {
                result.add(format.getName());
            }
        }

        return result;
    }

    @Override
    public List<String> getMailMergeExtensions() {
        List<String> result = new ArrayList<>();

        for (Format format : getFormats()) {
            if (DocumentType.CELL.equals(format.getType())) {
                result.add(format.getName());
            }
        }

        return result;
    }

    @Override
    public long getMaxFileSize() {
        long size = settingsManager.getDocsIntegrationSdkProperties()
                    .getDocumentServer()
                    .getEditingService()
                    .getMaxFileSize();

        return size > 0 ? size : DEFAULT_MAX_FILE_SIZE;
    }

    @Override
    public long getMaxConversionFileSize() {
        long size = settingsManager.getDocsIntegrationSdkProperties()
                .getDocumentServer()
                .getConvertService()
                .getMaxFileSize();

        return size > 0 ? size : DEFAULT_MAX_FILE_SIZE;
    }

    @Override
    public boolean isForm(final InputStream inputStream) {
        try {
            // CHECKSTYLE:OFF
            byte[] bytes = new byte[300];
            int count = inputStream.read(bytes, 0, 300);
            // CHECKSTYLE:ON
            String pBuffer = new String(bytes, Charset.forName("Windows-1252"));

            int indexFirst = pBuffer.indexOf("%\315\312\322\251\015");
            if (indexFirst == -1) {
                return false;
            }

            // CHECKSTYLE:OFF
            String pFirst = pBuffer.substring(indexFirst + 6);
            // CHECKSTYLE:ON
            if (!pFirst.startsWith("1 0 obj\012<<\012")) {
                return false;
            }

            // CHECKSTYLE:OFF
            pFirst = pFirst.substring(11);
            // CHECKSTYLE:ON

            int indexStream = pFirst.indexOf("stream\015\012");
            int indexMeta = pFirst.indexOf("ONLYOFFICEFORM");

            if (indexStream == -1 || indexMeta == -1 || indexStream < indexMeta) {
                return false;
            }

            String pMeta = pFirst.substring(indexMeta);
            // CHECKSTYLE:OFF
            pMeta = pMeta.substring("ONLYOFFICEFORM".length() + 3);
            // CHECKSTYLE:ON

            int indexMetaLast = pMeta.indexOf(" ");

            if (indexMetaLast == -1) {
                return false;
            }

            pMeta = pMeta.substring(indexMetaLast + 1);

            indexMetaLast = pMeta.indexOf(" ");

            if (indexMetaLast == -1) {
                return false;
            }

            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected static void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputStream =  Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(DOCS_FORMATS_JSON_PATH);

        if (inputStream == null) {
            inputStream = DefaultDocumentManager.class
                    .getClassLoader()
                    .getResourceAsStream(DOCS_FORMATS_JSON_PATH);
        }


        try {
            formats = objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                    .readValue(inputStream, new TypeReference<List<Format>>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
