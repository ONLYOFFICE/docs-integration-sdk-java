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

import com.onlyoffice.model.common.Format;
import com.onlyoffice.model.documenteditor.config.document.DocumentType;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface DocumentManager {

    /**
     * Returns a list of file formats (electronic documents, forms, spreadsheets, presentations) supported
     * by the ONLYOFFICE editors. The properties of each file format type are described in the list. This information
     * is taken from the <a target="_top"
     * href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>.
     *
     * @return A list containing data about the supported formats.
     */
    List<Format> getFormats();

    /**
     * Generates a unique document identifier used by the service to recognize the document.
     *
     * @param fileId The file ID.
     * @param embedded Specifies if the editor is opened in the embedded mode (true) or not (false).
     * @return The unique document identifier.
     */
    String getDocumentKey(String fileId, boolean embedded);

    /**
     * Returns the document name by file ID.
     *
     * @param fileId The file ID.
     * @return The document name.
     */
    String getDocumentName(String fileId);

    /**
     * Returns the file extension from the file name.
     *
     * @param fileName The file name to get the extension.
     * @return The file extension, or null if it doesn't exist, or the file name is empty.
     */
    String getExtension(String fileName);


    /**
     * Returns the file base name without the full path and extension.
     *
     * @param fileName The file name to get the base name.
     * @return The file name without the extension or null if the file name is empty.
     */
    String getBaseName(String fileName);

    /**
     * Returns the document type by the file name.
     *
     * @param fileName The file name to get the document type.
     * @see DocumentType
     * @return The document type.
     */
    DocumentType getDocumentType(String fileName);

    /**
     * Determines whether a document with a name specified in the request is editable.
     *
     * @param fileName The file name.
     * @return True if the document is editable.
     */
    boolean isEditable(String fileName);

    /**
     * Determines whether a document with a name specified in the request is viewable.
     *
     * @param fileName The file name.
     * @return True if the document is viewable.
     */
    boolean isViewable(String fileName);

    /**
     * Determines whether a document with a name specified in the request is fillable.
     *
     * @param fileName The file name.
     * @return True if the document is fillable.
     */
    boolean isFillable(String fileName);

    /**
     * Determines whether an action can be performed on a document with a name specified in the request.
     *
     * @param fileName The file name.
     * @param action An action to perform.
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>
     * @return True if the action can be performed on a document.
     */
    boolean hasAction(String fileName, String action);

    /**
     * Returns {@link InputStream} of an empty file with the given extension and locale.
     *
     * @param extension The file extension.
     * @param locale The file locale.
     * @see <a target="_top"
     * href="https://github.com/ONLYOFFICE/document-templates/tree/main/new">Template repository</a>
     * @return {@link InputStream} of an empty file.
     */
    InputStream getNewBlankFile(String extension, Locale locale);

    /**
     * Returns the default OOXML extension by the specified document type.
     *
     * @param documentType The document type.
     * @see DocumentType
     * @return The default OOXML extension.
     */
    String getDefaultExtension(DocumentType documentType);

    /**
     * Returns the default OOXML extension to which the document with the name specified in the request should be
     * converted.
     *
     * @param fileName The file name.
     * @return The default OOXML extension for conversion.
     */
    String getDefaultConvertExtension(String fileName);

    /**
     * Returns a list of extensions that the document with the name specified in the request can be converted to.
     *
     * @param fileName The file name.
     * @return A list of extensions for conversion.
     */
    List<String> getConvertExtensionList(String fileName);

    /**
     * Returns a map of extensions that can be edited with the possible loss of information.
     * If the extension is contained in the "formats.lossy-edit" settings, the value is "true".
     *
     * @return A map of extensions that can be edited with the possible loss of information.
     */
    Map<String, Boolean> getLossyEditableMap();

    /**
     * Returns a list of image extensions that can be inserted into the document.
     *
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/methods#insertImage">
     *     insertImage method in API ONLYOFFICE</a>
     * @return A list of image extensions.
     */
    List<String> getInsertImageExtensions();

    /**
     * Returns a list of extensions that can be used for the document comparison functions.
     *
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/methods#setRequestedDocument">
     *     setRequestedDocument method in API ONLYOFFICE</a>
     * @return A list of extensions for the document comparison.
     */
    List<String> getCompareFileExtensions();

    /**
     * Returns a list of extensions that can be used for mail merge in the document.
     *
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/methods#setRequestedSpreadsheet">
     *     setRequestedSpreadsheet method in API ONLYOFFICE</a>
     * @return A list of extensions for mail merge.
     */
    List<String> getMailMergeExtensions();


    /**
     * Returns the maximum file size that can be opened in the ONLYOFFICE editor.
     *
     * @return The maximum file size that can be opened.
     */
    long getMaxFileSize();

    /**
     * Returns the maximum file size that can be converted in the ONLYOFFICE Conversion Service.
     *
     * @return The maximum file size that can be converted.
     */
    long getMaxConversionFileSize();
}

