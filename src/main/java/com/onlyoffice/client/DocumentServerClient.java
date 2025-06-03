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

package com.onlyoffice.client;

import com.onlyoffice.model.commandservice.CommandRequest;
import com.onlyoffice.model.commandservice.CommandResponse;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.convertservice.ConvertResponse;
import com.onlyoffice.model.docbuilderservice.DocBuilderRequest;
import com.onlyoffice.model.docbuilderservice.DocBuilderResponse;
import com.onlyoffice.model.settings.Settings;

import java.io.OutputStream;

public interface DocumentServerClient {
    /**
     * Applies the specified settings to the document server client.
     *
     * @param settings The settings object containing configuration parameters to be applied
     * @throws IllegalArgumentException if the settings parameter is null
     */
    void applySettings(Settings settings);

    /**
     * Applies the specified document server client settings.
     *
     * @param documentServerClientSettings The document server client settings to be applied
     * @throws IllegalArgumentException if the documentServerClientSettings parameter is null
     */
    void applySettings(DocumentServerClientSettings documentServerClientSettings);

    /**
     * Performs a health check of the document server.
     *
     * @return Boolean value indicating the health status of the server:
     *         true if the server is healthy and operational,
     *         false if the server is experiencing issues
     */
    Boolean healthcheck();

    /**
     * Retrieves a file from the specified path as a byte array.
     *
     * @param fileUrl The fileUrl to the file to be retrieved
     * @return byte array containing the file contents
     */
    byte[] getFile(String fileUrl);

    /**
     * Retrieves a file from the specified path and writes it to the provided output stream.
     *
     * @param fileUrl The fileUrl to the file to be retrieved
     * @param outputStream The output stream to write the file contents to
     * @return The number of bytes written to the output stream
     */
    int getFile(String fileUrl, OutputStream outputStream);

    /**
     * Converts a document according to the specified conversion parameters.
     *
     * @param convertRequest The conversion request containing parameters and settings
     * @return ConvertResponse object containing the conversion result
     */
    ConvertResponse convert(ConvertRequest convertRequest);

    /**
     * Executes a command on the document server.
     *
     * @param commandRequest The command request containing the command and parameters
     * @return CommandResponse object containing the command execution result
     */
    CommandResponse command(CommandRequest commandRequest);

    /**
     * Executes a DocBuilder request to perform document processing operations.
     *
     * @param docBuilderRequest The request containing DocBuilder instructions and parameters
     * @return DocBuilderResponse The response containing the result of the DocBuilder operation
     */
    DocBuilderResponse docbuilder(DocBuilderRequest docBuilderRequest);
}
