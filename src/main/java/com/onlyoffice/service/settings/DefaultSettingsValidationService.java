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

package com.onlyoffice.service.settings;

import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.commandservice.CommandRequest;
import com.onlyoffice.model.commandservice.CommandResponse;
import com.onlyoffice.model.commandservice.commandrequest.Command;
import com.onlyoffice.model.common.CommonResponse;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.convertservice.ConvertResponse;
import com.onlyoffice.model.security.Credentials;
import com.onlyoffice.model.settings.validation.ValidationResult;
import com.onlyoffice.model.settings.validation.status.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter(AccessLevel.PROTECTED)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultSettingsValidationService implements SettingsValidationService {
    /** {@link RequestManager}. */
    private RequestManager requestManager;
    /** {@link UrlManager}. */
    private UrlManager urlManager;
    /** {@link SettingsManager}. */
    private SettingsManager settingsManager;

    @Override
    public ValidationResult checkDocumentServer() throws Exception {
        return checkDocumentServer(urlManager.getInnerDocumentServerUrl());
    }

    @Override
    public ValidationResult checkDocumentServer(final String url) throws Exception {
        String healthCheckUrl = settingsManager.getSDKSetting("integration-sdk.service.health-check.url");

        HttpGet request = new HttpGet(urlManager.sanitizeUrl(url) + healthCheckUrl);

        return requestManager.executeRequest(request,
                new RequestManager.Callback<ValidationResult>() {
            public ValidationResult doWork(final HttpEntity httpEntity) throws IOException {
                String content = IOUtils.toString(httpEntity.getContent(), "utf-8").trim();
                if (content.equalsIgnoreCase("true")) {
                    return ValidationResult.builder()
                            .status(Status.SUCCESS)
                            .build();
                } else {
                    return ValidationResult.builder()
                            .status(Status.FAILED)
                            .errorCode(CommonResponse.Error.HEALTHCHECK_ERROR)
                            .build();
                }
            }
        });
    }

    @Override
    public ValidationResult checkCommandService() throws Exception {
        String url = urlManager.getInnerDocumentServerUrl();
        Credentials credentials = Credentials.builder()
                .key(settingsManager.getSecuritySecret())
                .header(settingsManager.getSecurityHeader())
                .prefix(settingsManager.getSecurityPrefix())
                .build();

        return checkCommandService(url, credentials);
    }

    @Override
    public ValidationResult checkCommandService(final String url, final Credentials credentials) throws Exception {
        String commandServiceUrl = settingsManager.getSDKSetting("integration-sdk.service.command.url");

        CommandRequest commandRequest = CommandRequest.builder()
                .c(Command.VERSION)
                .build();

        return requestManager.executePostRequest(
                urlManager.sanitizeUrl(url) + commandServiceUrl,
                commandRequest,
                credentials,
                new RequestManager.Callback<ValidationResult>() {
                    public ValidationResult doWork(final HttpEntity httpEntity) throws IOException {
                        String content = IOUtils.toString(httpEntity.getContent(), "utf-8").trim();
                        JSONObject result = new JSONObject(content);
                        if (result.has("error") && result.getInt("error") == 0) {
                            return ValidationResult.builder()
                                    .status(Status.SUCCESS)
                                    .build();
                        } else {
                            Integer errorCode = result.getInt("error");

                            return ValidationResult.builder()
                                    .status(Status.FAILED)
                                    .errorCode(CommandResponse.Error.valueOfCode(errorCode))
                                    .build();
                        }
                    }
                });
    }

    @Override
    public ValidationResult checkConvertService() throws Exception {
        String url = urlManager.getInnerDocumentServerUrl();
        Credentials credentials = Credentials.builder()
                .key(settingsManager.getSecuritySecret())
                .header(settingsManager.getSecurityHeader())
                .prefix(settingsManager.getSecurityPrefix())
                .build();

        return checkConvertService(url, credentials);
    }

    @Override
    public ValidationResult checkConvertService(final String url, final Credentials credentials) throws Exception {
        String convertServiceUrl = settingsManager.getSDKSetting("integration-sdk.service.convert.url");

        ConvertRequest convertRequest = ConvertRequest.builder()
                .async(false)
                .filetype("txt")
                .outputtype("docx")
                .key(new SimpleDateFormat("MMddyyyyHHmmss").format(new Date()))
                .url(urlManager.getTestConvertUrl())
                .build();

        return requestManager.executePostRequest(
                urlManager.sanitizeUrl(url) + convertServiceUrl,
                convertRequest,
                credentials,
                new RequestManager.Callback<ValidationResult>() {
                    public ValidationResult doWork(final HttpEntity httpEntity) throws Exception {
                        String content = IOUtils.toString(httpEntity.getContent(), "utf-8").trim();
                        JSONObject result = new JSONObject(content);

                        if (result.has("error")) {
                            Integer errorCode = result.getInt("error");

                            return ValidationResult.builder()
                                    .status(Status.FAILED)
                                    .errorCode(ConvertResponse.Error.valueOfCode(errorCode))
                                    .build();
                        }

                        String fileUrl = result.getString("fileUrl");

                        HttpGet request = new HttpGet(fileUrl);
                        return requestManager.executeRequest(
                                request,
                                new RequestManager.Callback<ValidationResult>() {
                            @Override
                            public ValidationResult doWork(final HttpEntity httpEntity) throws IOException {
                                byte[] bytes = EntityUtils.toByteArray(httpEntity);
                                if (bytes.length > 0) {
                                    return ValidationResult.builder()
                                            .status(Status.SUCCESS)
                                            .build();
                                } else {
                                    return ValidationResult.builder()
                                            .status(Status.FAILED)
                                            .errorCode(CommonResponse.Error.DOWNLOAD_RESULT_ERROR)
                                            .build();
                                }
                            }
                        });

                    }
                });
    }
}