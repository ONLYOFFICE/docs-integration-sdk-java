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
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.service.CommandServiceErrorCode;
import com.onlyoffice.model.service.ConvertServiceErrorCode;
import com.onlyoffice.model.service.DocumentServerErrorCode;
import com.onlyoffice.model.service.Service;
import com.onlyoffice.model.settings.validation.ValidationResult;
import com.onlyoffice.model.settings.validation.status.Status;
import lombok.AllArgsConstructor;
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
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultValidationSettingsService implements ValidationSettingsService {

    protected RequestManager requestManager;
    protected UrlManager urlManager;

    public List<ValidationResult> validateSettings(String url, String secretKey, String jwtHeader) {
        return null;
    }

    public ValidationResult checkDocumentServer(String url) throws Exception {
        HttpGet request = new HttpGet(urlManager.sanitizeUrl(url) + "/healthcheck");

        return requestManager.executeRequest(Service.DOCUMENT_SERVER, request,
                new RequestManager.Callback<ValidationResult>() {
            public ValidationResult doWork(HttpEntity httpEntity) throws IOException {
                String content = IOUtils.toString(httpEntity.getContent(), "utf-8").trim();
                if (content.equalsIgnoreCase("true")) {
                    return ValidationResult.builder()
                            .status(Status.SUCCESS)
                            .service(Service.DOCUMENT_SERVER)
                            .build();
                } else {
                    return ValidationResult.builder()
                            .status(Status.FAILED)
                            .service(Service.DOCUMENT_SERVER)
                            .errorCode(DocumentServerErrorCode.HEALTHCHECK_ERROR)
                            .build();
                }
            }
        });
    }

    public ValidationResult checkCommandService(String url, String secretKey, String jwtHeader) throws Exception {
        JSONObject body = new JSONObject();
        body.put("c", "version");

        return requestManager.executePostRequest(
                Service.COMMAND_SERVICE,
                body,
                url,
                secretKey,
                jwtHeader,
                new RequestManager.Callback<ValidationResult>() {
                    public ValidationResult doWork(HttpEntity httpEntity) throws IOException {
                        String content = IOUtils.toString(httpEntity.getContent(), "utf-8").trim();
                        JSONObject result = new JSONObject(content);
                        if (result.has("error") && result.getInt("error") == 0) {
                            return ValidationResult.builder()
                                    .status(Status.SUCCESS)
                                    .service(Service.COMMAND_SERVICE)
                                    .build();
                        } else {
                            Integer errorCode = result.getInt("error");

                            return ValidationResult.builder()
                                    .status(Status.FAILED)
                                    .service(Service.COMMAND_SERVICE)
                                    .errorCode(CommandServiceErrorCode.valueOfCode(errorCode))
                                    .build();
                        }
                    }
                });
    }

    public ValidationResult checkConvertService(String url, String secretKey, String jwtHeader) throws Exception {
        JSONObject body = new JSONObject();
        body.put("async", false);
        body.put("embeddedfonts", true);
        body.put("filetype", "txt");
        body.put("outputtype", "docx");
        body.put("key", new SimpleDateFormat("MMddyyyyHHmmss").format(new Date()));
        body.put("url",  urlManager.getTestConvertUrl());

        return requestManager.executePostRequest(
                Service.CONVERT_SERVICE,
                body,
                url,
                secretKey,
                jwtHeader,
                new RequestManager.Callback<ValidationResult>() {
                    public ValidationResult doWork(HttpEntity httpEntity) throws Exception {
                        String content = IOUtils.toString(httpEntity.getContent(), "utf-8").trim();
                        JSONObject result = new JSONObject(content);

                        if (result.has("error")) {
                            Integer errorCode = result.getInt("error");

                            return ValidationResult.builder()
                                    .status(Status.FAILED)
                                    .service(Service.CONVERT_SERVICE)
                                    .errorCode(ConvertServiceErrorCode.valueOfCode(errorCode))
                                    .build();
                        }

                        String fileUrl = result.getString("fileUrl");

                        return requestManager.executeGetRequest(fileUrl, new RequestManager.Callback<ValidationResult>() {
                            @Override
                            public ValidationResult doWork(HttpEntity httpEntity) throws IOException {
                                byte[] bytes = EntityUtils.toByteArray(httpEntity);
                                if (bytes.length > 0) {
                                    return ValidationResult.builder()
                                            .status(Status.SUCCESS)
                                            .service(Service.CONVERT_SERVICE)
                                            .build();
                                } else {
                                    return ValidationResult.builder()
                                            .status(Status.FAILED)
                                            .service(Service.CONVERT_SERVICE)
                                            .errorCode(ConvertServiceErrorCode.DOWNLOAD_RESULT_ERROR)
                                            .build();
                                }
                            }
                        });

                    }
                });
    }
}
