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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.settings.SettingsManager;
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
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@AllArgsConstructor
public class DefaultSettingsValidationService implements SettingsValidationService {

    /** {@link RequestManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private RequestManager requestManager;

    /** {@link UrlManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private UrlManager urlManager;

    /** {@link SettingsManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private SettingsManager settingsManager;

    /** {@link ObjectMapper}. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ValidationResult checkDocumentServer() throws Exception {
        HttpClientSettings httpClientSettings = HttpClientSettings.builder()
                .ignoreSSLCertificate(settingsManager.isIgnoreSSLCertificate())
                .build();

        return checkDocumentServer(urlManager.getInnerDocumentServerUrl(), httpClientSettings);
    }

    @Override
    public ValidationResult checkDocumentServer(final String url, final HttpClientSettings httpClientSettings)
            throws Exception {
        String healthCheckUrl = settingsManager.getDocsIntegrationSdkProperties()
                .getDocumentServer()
                .getHealthCheckUrl();

        healthCheckUrl = urlManager.sanitizeUrl(url) + healthCheckUrl;

        return requestManager.executeGetRequest(healthCheckUrl,
                httpClientSettings,
                new RequestManager.Callback<ValidationResult>() {
            public ValidationResult doWork(final Object response) throws IOException {
                String content = IOUtils.toString(((HttpEntity) response).getContent(), "utf-8").trim();
                if (content.equalsIgnoreCase("true")) {
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
        });
    }

    @Override
    public ValidationResult checkCommandService() throws Exception {
        String url = urlManager.getInnerDocumentServerUrl();
        Security security = Security.builder()
                .key(settingsManager.getSecurityKey())
                .header(settingsManager.getSecurityHeader())
                .prefix(settingsManager.getSecurityPrefix())
                .build();

        HttpClientSettings httpClientSettings = HttpClientSettings.builder()
                .ignoreSSLCertificate(settingsManager.isIgnoreSSLCertificate())
                .build();

        return checkCommandService(url, security, httpClientSettings);
    }

    @Override
    public ValidationResult checkCommandService(final String url, final Security security,
                                                final HttpClientSettings httpClientSettings) throws Exception {
        String commandServiceUrl = settingsManager.getDocsIntegrationSdkProperties()
                .getDocumentServer()
                .getCommandService()
                .getUrl();

        CommandRequest commandRequest = CommandRequest.builder()
                .c(Command.VERSION)
                .build();

        return requestManager.executePostRequest(
                urlManager.sanitizeUrl(url) + commandServiceUrl,
                commandRequest,
                security,
                httpClientSettings,
                new RequestManager.Callback<ValidationResult>() {
                    public ValidationResult doWork(final Object response) throws IOException {
                        String content = IOUtils.toString(((HttpEntity) response).getContent(), "utf-8");

                        CommandResponse commandResponse = objectMapper.readValue(content, CommandResponse.class);

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
                });
    }

    @Override
    public ValidationResult checkConvertService() throws Exception {
        String url = urlManager.getInnerDocumentServerUrl();
        Security security = Security.builder()
                .key(settingsManager.getSecurityKey())
                .header(settingsManager.getSecurityHeader())
                .prefix(settingsManager.getSecurityPrefix())
                .build();

        HttpClientSettings httpClientSettings = HttpClientSettings.builder()
                .ignoreSSLCertificate(settingsManager.isIgnoreSSLCertificate())
                .build();

        return checkConvertService(url, null, security, httpClientSettings);
    }

    @Override
    public ValidationResult checkConvertService(final String productUrl) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }


    @Override
    public ValidationResult checkConvertService(final String url, final String productInnerUrl, final Security security,
                                                final HttpClientSettings httpClientSettings)
            throws Exception {
        String convertServiceUrl = settingsManager.getDocsIntegrationSdkProperties()
                .getDocumentServer()
                .getConvertService()
                .getUrl();

        ConvertRequest convertRequest = ConvertRequest.builder()
                .async(false)
                .filetype("txt")
                .outputtype("docx")
                .key(new SimpleDateFormat("MMddyyyyHHmmss").format(new Date()))
                .url(urlManager.getTestConvertUrl(productInnerUrl))
                .build();

        return requestManager.executePostRequest(
                urlManager.sanitizeUrl(url) + convertServiceUrl,
                convertRequest,
                security,
                httpClientSettings,
                new RequestManager.Callback<ValidationResult>() {
                    public ValidationResult doWork(final Object response) throws Exception {
                        String content = IOUtils.toString(((HttpEntity) response).getContent(), "utf-8").trim();
                        JSONObject result = new JSONObject(content);

                        if (result.has("error")) {
                            Integer errorCode = result.getInt("error");

                            return ValidationResult.builder()
                                    .status(Status.FAILED)
                                    .error(ConvertResponse.Error.valueOfCode(errorCode))
                                    .build();
                        }

                        String fileUrl = result.getString("fileUrl");

                        return requestManager.executeGetRequest(
                                fileUrl,
                                httpClientSettings,
                                new RequestManager.Callback<ValidationResult>() {
                            @Override
                            public ValidationResult doWork(final Object response) throws IOException {
                                byte[] bytes = EntityUtils.toByteArray((HttpEntity) response);
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
                            }
                        });

                    }
                });
    }
}
