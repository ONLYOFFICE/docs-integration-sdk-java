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

package com.onlyoffice.service.settings;

import com.onlyoffice.client.DocumentServerClient;
import com.onlyoffice.exception.DocumentServerResponseException;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.commandservice.CommandRequest;
import com.onlyoffice.model.commandservice.CommandResponse;
import com.onlyoffice.model.commandservice.commandrequest.Command;
import com.onlyoffice.model.common.CommonResponse;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.convertservice.ConvertResponse;
import com.onlyoffice.model.settings.HttpClientSettings;
import com.onlyoffice.model.settings.security.Security;
import com.onlyoffice.model.settings.validation.ValidationResult;
import com.onlyoffice.model.settings.validation.status.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
public class DefaultSettingsValidationServiceV2 implements SettingsValidationService {

    /** {@link DocumentServerClient}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private DocumentServerClient documentServerClient;

    /** {@link DocumentServerClient}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private UrlManager urlManager;

    @Override
    public ValidationResult checkDocumentServer() {
        Boolean healthcheck = documentServerClient.healthcheck();

        if (healthcheck) {
            return ValidationResult.builder()
                    .status(Status.SUCCESS)
                    .build();
        } else {
            return ValidationResult.builder()
                    .status(Status.FAILED)
                    .error(CommonResponse.Error.HEALTHCHECK)
                    .build();
        }
    }

    @Override
    public ValidationResult checkConvertService(final String productUrl) {
        String testConvertUrl = urlManager.getTestConvertUrl(productUrl);

        ConvertRequest convertRequest = ConvertRequest.builder()
                .async(false)
                .filetype("txt")
                .outputtype("docx")
                .key(new SimpleDateFormat("MMddyyyyHHmmss").format(new Date()))
                .url(testConvertUrl)
                .build();

        ConvertResponse convertResponse = documentServerClient.convert(convertRequest);

        if (convertResponse.getError() != null) {
            return ValidationResult.builder()
                    .status(Status.FAILED)
                    .error(convertResponse.getError())
                    .build();
        }

        String fileUrl = convertResponse.getFileUrl();

        try {
            byte[] bytes = documentServerClient.getFile(fileUrl);

            if (bytes.length > 0) {
                return ValidationResult.builder()
                        .status(Status.SUCCESS)
                        .build();
            } else {
                return ValidationResult.builder()
                        .status(Status.FAILED)
                        .error(CommonResponse.Error.DOWNLOAD_RESULT)
                        .build();
            }
        } catch (DocumentServerResponseException e) {
            return ValidationResult.builder()
                    .status(Status.FAILED)
                    .error(CommonResponse.Error.DOWNLOAD_RESULT)
                    .build();
        }
    }

    @Override
    public ValidationResult checkCommandService() {
        CommandRequest commandRequest = CommandRequest.builder()
                .c(Command.VERSION)
                .build();

        CommandResponse commandResponse = documentServerClient.command(commandRequest);

        if (commandResponse.getError() != null && commandResponse.getError().equals(
                CommandResponse.Error.NO)) {
            return ValidationResult.builder()
                    .status(Status.SUCCESS)
                    .build();
        } else {
            return ValidationResult.builder()
                    .status(Status.FAILED)
                    .error(commandResponse.getError())
                    .build();
        }
    }

    @Override
    public ValidationResult checkDocumentServer(final String url, final HttpClientSettings httpClientSettings) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public ValidationResult checkConvertService() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public ValidationResult checkConvertService(final String url, final String productInnerUrl, final Security security,
                                                final HttpClientSettings httpClientSettings) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public ValidationResult checkCommandService(final String url, final Security security,
                                                final HttpClientSettings httpClientSettings) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}
