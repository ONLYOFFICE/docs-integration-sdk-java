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

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.config.document.DocumentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentManagerTest {
    @Mock
    private SettingsManager settingsManager;

    private DocumentManager documentManager;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        documentManager = new DefaultDocumentManager(settingsManager) {
            @Override
            public String getDocumentKey(final String fileId, final boolean embedded) {
                return fileId + "_" + embedded;
            }

            @Override
            public String getDocumentName(final String fileId) {
                return "sample.docx";
            }
        };
    }

    @Test
    void getFileExtensionTest() {
        Assertions.assertEquals("docx", documentManager.getExtension("sample.docx"));
        Assertions.assertEquals("docx", documentManager.getExtension(".docx"));
        Assertions.assertEquals(null, documentManager.getExtension("sample."));
        Assertions.assertEquals(null, documentManager.getExtension("sample"));
    }

    @Test
    void getBaseNameTest() {
        Assertions.assertEquals("sample", documentManager.getBaseName("sample.docx"));
        Assertions.assertEquals(null, documentManager.getBaseName(".docx"));
        Assertions.assertEquals("sample", documentManager.getBaseName("sample."));
        Assertions.assertEquals("sample", documentManager.getBaseName("sample"));
    }

    @Test
    void getDocumentTypeTest() {
        Assertions.assertEquals(DocumentType.WORD, documentManager.getDocumentType("sample.docx"));
        Assertions.assertEquals(DocumentType.CELL, documentManager.getDocumentType("sample.xlsx"));
        Assertions.assertEquals(DocumentType.SLIDE, documentManager.getDocumentType("sample.pptx"));
        Assertions.assertEquals(null, documentManager.getDocumentType("sample.null"));
        Assertions.assertEquals(DocumentType.WORD, documentManager.getDocumentType(".docx"));
        Assertions.assertEquals(null, documentManager.getDocumentType("sample."));
        Assertions.assertEquals(null, documentManager.getDocumentType("sample"));
    }

    @Test
    void isEditableTest() {
        Assertions.assertEquals(true, documentManager.isEditable("sample.docx"));
        Assertions.assertEquals(true, documentManager.isEditable(".docx"));
        Assertions.assertEquals(false, documentManager.isEditable("docx"));
        Assertions.assertEquals(false, documentManager.isEditable("sample.null"));
        Assertions.assertEquals(false, documentManager.isEditable("sample."));
    }

    @Test
    void isViewableTest() {
        Assertions.assertEquals(true, documentManager.isViewable("sample.docx"));
        Assertions.assertEquals(true, documentManager.isViewable(".docx"));
        Assertions.assertEquals(false, documentManager.isViewable("docx"));
        Assertions.assertEquals(false, documentManager.isViewable("sample.null"));
        Assertions.assertEquals(false, documentManager.isViewable("sample."));
    }

    @Test
    void isFillableTest() {
        Assertions.assertEquals(true, documentManager.isFillable("sample.oform"));
        Assertions.assertEquals(true, documentManager.isFillable(".oform"));
        Assertions.assertEquals(false, documentManager.isFillable("oform"));
        Assertions.assertEquals(false, documentManager.isFillable("sample.null"));
        Assertions.assertEquals(false, documentManager.isFillable("sample."));
    }

    @Test
    void hasActionTest() {
        Assertions.assertEquals(true, documentManager.hasAction("sample.docx", "edit"));
        Assertions.assertEquals(true, documentManager.hasAction("sample.docx", "view"));
        Assertions.assertEquals(false, documentManager.hasAction("sample.docx", "lossy-edit"));
        Assertions.assertEquals(true, documentManager.hasAction("sample.txt", "lossy-edit"));
    }

    @Test
    void getNewBlankFileTest() throws IOException {
        Assertions.assertEquals(
                true,
                documentManager.getNewBlankFile("docx", Locale.ENGLISH).available() > 0
        );

        Assertions.assertEquals(
                null,
                documentManager.getNewBlankFile("null", Locale.ENGLISH)
        );
    }

    @Test
    void getDefaultExtensionTest() {
        Assertions.assertEquals("docx", documentManager.getDefaultExtension(DocumentType.WORD));
        Assertions.assertEquals(null, documentManager.getDefaultExtension(null));
    }

    @Test
    void getDefaultConvertExtensionTest() {
        Assertions.assertEquals("docx", documentManager.getDefaultConvertExtension("sample.txt"));
        Assertions.assertEquals("xlsx", documentManager.getDefaultConvertExtension("sample.csv"));
        Assertions.assertEquals(null, documentManager.getDefaultConvertExtension("sample.mull"));
    }
    @Test
    void getConvertExtensionListTest() {
        Assertions.assertEquals(
                true, !documentManager.getDefaultConvertExtension("sample.txt").isEmpty()
        );
    }

    @Test
    void getInsertImageExtensionsTest() {
        when(settingsManager.getSDKSetting("integration-sdk.data.formats.insert-image")).thenReturn("png|jpg");
        List<String> insertImageExtensions = documentManager.getInsertImageExtensions();

        Assertions.assertEquals(true, insertImageExtensions.contains("png"));
        Assertions.assertEquals(true, insertImageExtensions.contains("jpg"));
    }

    @Test
    void getCompareFileExtensionsTest() {
        List<String> compareFileExtensions = documentManager.getCompareFileExtensions();

        Assertions.assertEquals(true, !compareFileExtensions.isEmpty());
    }

    @Test
    void getMailMergeExtensionsTest() {
        List<String> mailMergeExtensions = documentManager.getMailMergeExtensions();

        Assertions.assertEquals(true, !mailMergeExtensions.isEmpty());
    }

    @Test
    void getMaxFileSizeTest() {
        when(settingsManager.getSDKSetting("integration-sdk.data.filesize.editing.max")).thenReturn("104857600");
        Long maxFileSize = documentManager.getMaxFileSize();

        Assertions.assertEquals(104857600L, maxFileSize);
    }

    @Test
    void getMaxConversionFileSizeTest() {
        when(settingsManager.getSDKSetting("integration-sdk.data.filesize.conversion.max")).thenReturn("104857600");
        Long maxConversionFileSize = documentManager.getMaxConversionFileSize();

        Assertions.assertEquals(104857600L, maxConversionFileSize);
    }
}
