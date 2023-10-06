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
import com.onlyoffice.model.config.document.documenttype.DocumentType;
import com.onlyoffice.model.format.Format;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DefaultDocumentManager implements DocumentManager {
    private static final int DEFAULT_MAX_FILE_SIZE = 5242880;

    @Getter(AccessLevel.PROTECTED)
    private SettingsManager settingsManager;

    private static List<Format> formats;

    static {
        init();
    }

    public List<Format> getFormats() {
        return this.formats;
    }

    public abstract String getDocumentKey(String fileId, boolean embedded);

    public abstract String getDocumentName(String fileId);

    public String getExtension(final String fileName) {
        return FilenameUtils.getExtension(fileName).toLowerCase();
    }

    public String geBaseName(final String fileName) {
        return FilenameUtils.getBaseName(fileName);
    }

    public String getMimeType(final String fileName) {
        Path path = new File(fileName).toPath();

        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    public DocumentType getDocumentType(final String fileName) {
        String fileExtension = getExtension(fileName);

        for (Format format : this.formats) {
            if (format.getName().equals(fileExtension)) {
                return format.getType();
            }
        }

        return null;
    }

    public boolean isEditable(final String fileName) {
        return hasAction(fileName, "edit");
    }

    public boolean isViewable(final String fileName) {
        return hasAction(fileName, "view");
    }

    public boolean isFillable(final String fileName) {
        return hasAction(fileName, "fill");
    }


    public boolean hasAction(final String fileName, final String action) {
        String fileExtension = getExtension(fileName);

        for (Format format : this.formats) {
            if (format.getName().equals(fileExtension) && format.getActions().contains(action)) {
                return true;
            }
        }

        return false;
    }

    public InputStream getNewBlankFile(final String extension, final Locale locale) {
        String path = "assets/document-templates/" + locale.toLanguageTag() + "/new." + extension;

        if (this.getClass().getClassLoader().getResourceAsStream(path) == null) {
            path = "assets/document-templates/en-US/new." + extension;
        }

        return this.getClass()
                .getClassLoader()
                .getResourceAsStream(path);
    }

    public String getDefaultExtension(final DocumentType documentType) {
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

    public String getDefaultConvertExtension(final String fileName) {
        String extension = getExtension(fileName);

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

    public List<String> getConvertExtensionList(final String fileName) {
        String extension = getExtension(fileName);

        for (Format format : formats) {
            if (format.getName().equals(extension)) {
                return format.getConvert();
            }
        }

        return null;
    }

    public List<String> getLossyEditableExtensions() {
        List<String> result = new ArrayList<>();

        for (Format format : formats) {
            if (format.getActions().contains("lossy-edit")) {
                result.add(format.getName());
            }
        }

        return result;
    }

    public List<String> getInsertImageExtension() {
        String insertImage = settingsManager.getSDKSetting("integration-sdk.data.formats.insert-image");

        if (insertImage != null && !insertImage.isEmpty()) {
            Arrays.asList(insertImage.split("|"));
        }

        return null;
    }

    public List<String> getCompareFileExtension() {
        List<Format> supportedFormats = formats;
        List<String> result = new ArrayList<>();

        for (Format format : supportedFormats) {
            if (format.getType().equals(DocumentType.WORD)) {
                result.add(format.getName());
            }
        }

        return result;
    }

    public List<String> getMailMergeExtension() {
        List<Format> supportedFormats = formats;
        List<String> result = new ArrayList<>();

        for (Format format : supportedFormats) {
            if (format.getType().equals(DocumentType.CELL)) {
                result.add(format.getName());
            }
        }

        return result;
    }

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
