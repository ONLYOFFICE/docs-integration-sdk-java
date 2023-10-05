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

import com.onlyoffice.model.config.document.documenttype.DocumentType;
import com.onlyoffice.model.format.Format;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface DocumentManager {

    /**
     * Returns list containing of file formats (electronic documents, forms, spreadsheets, presentations) supported
     * by ONLYOFFICE editors and describes the properties of each file format type, was taken from <a target="_top"
     * href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>.
     *
     * @return list containing data about supported formats
     */
    List<Format> getFormats();

    /**
     * Generate the unique document identifier used by the service to recognize the document.
     *
     * @param fileId the ID of the file
     * @param embedded set true if editor opened with embedded type
     * @return the unique document identifier
     */
    String getDocumentKey(String fileId, boolean embedded);

    /**
     * Gets the document name by file id.
     *
     * @param fileId the ID of the file
     * @return the document name
     */
    String getDocumentName(String fileId);

    /**
     * Gets the extension of a fileName.
     *
     * @param fileName the fileName to retrieve the extension of
     * @return the extension of the file or an empty string if none exists or null if the fileName is null.
     */
    String getExtension(String fileName);


    /**
     * Gets the base name, minus the full path and extension, from a full fileName.
     *
     * @param fileName the fileName to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    String geBaseName(String fileName);

    /**
     * Gets the content type by file name.
     *
     * @param fileName the fileName to query
     * @return the content type of the file, or "application/octet-stream" if the content type cannot be determined
     */
    String getMimeType(String fileName);

    /**
     * Gets the document type by file name.
     *
     * @param fileName the fileName to retrieve the document type of
     * @see DocumentType
     * @return the document type
     */
    DocumentType getDocumentType(String fileName);

    /**
     * Determine whether a document is editable by file name.
     *
     * @param fileName the file name to determine
     * @return true if document is editable
     */
    boolean isEditable(String fileName);

    /**
     * Determine whether a document is viewable by file name.
     *
     * @param fileName the file name to determine
     * @return true if document is viewable
     */
    boolean isViewable(String fileName);

    /**
     * Determine whether a document is fillable by file name.
     *
     * @param fileName the file name to determine
     * @return true if document is fillable
     */
    boolean isFillable(String fileName);

    /**
     * Determine whether an action can be performed on a document by file name.
     *
     * @param fileName the file name to determine
     * @param action the performed action
     * @see <a target="_top" href="https://github.com/ONLYOFFICE/document-formats">Formats repository</a>
     * @return true if action can be performed on a document
     */
    boolean hasAction(String fileName, String action);

    /**
     * Gets an {@link InputStream} of an empty file with the given extension and locale.
     *
     * @param extension the file extension
     * @param locale the locale
     * @see <a target="_top"
     * href="https://github.com/ONLYOFFICE/document-templates/tree/main/new">Template repository</a>
     * @return {@link InputStream} of an empty file
     */
    InputStream getNewBlankFile(String extension, Locale locale);

    /**
     * Determines the default OOXML extension by document type.
     *
     * @param documentType document type
     * @see DocumentType
     * @return default OOXML extension
     */
    String getDefaultExtension(DocumentType documentType);

    /**
     * Determine the default OOXML extension to which the document should be converted.
     *
     * @param fileName the file name to determine
     * @return default convert OOXML extension
     */
    String getDefaultConvertExtension(String fileName);

    /**
     * Determine a list of extensions that the document can be converted to.
     *
     * @param fileName the file name to determine
     * @return list extension
     */
    List<String> getConvertExtensionList(String fileName);

    /**
     * Get a map of extensions that can be edited with possible loss of information.
     *
     * @return map extension
     */
    List<String> Map<String, Boolean> getLossyEditableMap();();

    /**
     * Get a list of extensions that can be used for insert as image to document.
     *
     * @return list extension
     */
    List<String> getInsertImageExtensions();

    /**
     * Get a list of extensions that can be used for insert as image to document.
     *
     * @see <a target="_top" href="https://api.onlyoffice.com/editors/methods#insertImage">API ONLYOFFICE</a>
     * @return list extension
     */
    List<String> getCompareFileExtensions();

    /**
     * Get a list of extensions that can be used for document comparison functions.
     *
     * @see <a target="_top"
     * href="https://api.onlyoffice.com/editors/config/events#onRequestCompareFile">API ONLYOFFICE</a>
     * @return list extension
     */
    List<String> getMailMergeExtensions();


    /**
     * Get the maximum file size that can be opened in the ONLYOFFICE editor.
     *
     * @return file size
     */
    long getMaxFileSize();

    /**
     * Get the maximum file size that can be converted in the ONLYOFFICE Conversion Service.
     *
     * @return file size
     */
    long getMaxConversionFileSize();
}
