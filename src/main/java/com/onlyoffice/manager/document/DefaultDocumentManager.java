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

package com.onlyoffice.manager.document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.config.document.DocumentType;
import com.onlyoffice.model.format.Format;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DefaultDocumentManager implements DocumentManager {

    /**
     * Defines the default maximum file size, used if the "integration-sdk.data.filesize.editing.max"
     * or "integration-sdk.data.filesize.conversion.max" properties are not specified in "settings.properties".
     */
    private static final int DEFAULT_MAX_FILE_SIZE = 5242880;

    /** {@link SettingsManager}. */
    @Getter(AccessLevel.PROTECTED)
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

        for (Format format : this.formats) {
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

        for (Format format : this.formats) {
            if (format.getName().equals(fileExtension) && format.getActions().contains(action)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public InputStream getNewBlankFile(final String extension, final Locale locale) {
        String path = "assets/document-templates/" + locale.toLanguageTag() + "/new." + extension;

        if (this.getClass().getClassLoader().getResourceAsStream(path) == null) {
            path = "assets/document-templates/en-US/new." + extension;
        }

        return this.getClass()
                .getClassLoader()
                .getResourceAsStream(path);
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

        for (Format format : this.formats) {
            if (format.getName().equals(extension)) {
                switch (format.getType()) {
                    case WORD:
                        if (format.getName().equals("docxf") && format.getConvert().contains("oform")) {
                            return "oform";
                        }
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

        for (Format format : formats) {
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

        String formatsLossyEdit = settingsManager.getSetting("formats.lossy-edit");

        if (formatsLossyEdit != null && !formatsLossyEdit.isEmpty()) {
            formatsLossyEditList = Arrays.asList(formatsLossyEdit.split(","));
        }

        for (Format format : formats) {
            if (format.getActions().contains("lossy-edit")) {
                result.put(format.getName(), formatsLossyEditList.contains(format.getName()));
            }
        }

        return result;
    }

    public List<String> getInsertImageExtensions() {
        String insertImage = settingsManager.getSDKSetting("integration-sdk.data.formats.insert-image");

        if (insertImage != null && !insertImage.isEmpty()) {
            return Arrays.asList(insertImage.split("\\|"));
        }

        return null;
    }

    public List<String> getCompareFileExtensions() {
        List<Format> supportedFormats = formats;
        List<String> result = new ArrayList<>();

        for (Format format : supportedFormats) {
            if (format.getType().equals(DocumentType.WORD)) {
                result.add(format.getName());
            }
        }

        return result;
    }

    public List<String> getMailMergeExtensions() {
        List<Format> supportedFormats = formats;
        List<String> result = new ArrayList<>();

        for (Format format : supportedFormats) {
            if (format.getType().equals(DocumentType.CELL)) {
                result.add(format.getName());
            }
        }

        return result;
    }

    @Override
    public long getMaxFileSize() {
        long size;
        try {
            String filesizeMax = settingsManager.getSDKSetting("integration-sdk.data.filesize.editing.max");
            size = Long.parseLong(filesizeMax);
        } catch (Exception ex) {
            size = 0;
        }

        return size > 0 ? size : DEFAULT_MAX_FILE_SIZE;
    }

    @Override
    public long getMaxConversionFileSize() {
        long size;
        try {
            String filesizeMax = settingsManager.getSDKSetting("integration-sdk.data.filesize.conversion.max");
            size = Long.parseLong(filesizeMax);
        } catch (Exception ex) {
            size = 0;
        }

        return size > 0 ? size : DEFAULT_MAX_FILE_SIZE;
    }

    protected static void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputStream =  Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("assets/document-formats/onlyoffice-docs-formats.json");
        try {
            formats = objectMapper.readValue(inputStream, new TypeReference<List<Format>>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
