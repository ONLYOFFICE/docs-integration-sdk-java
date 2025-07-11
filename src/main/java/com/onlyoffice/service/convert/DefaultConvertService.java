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

package com.onlyoffice.service.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.convertservice.ConvertResponse;
import com.onlyoffice.model.convertservice.convertrequest.Thumbnail;
import com.onlyoffice.model.settings.HttpClientSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.HttpEntity;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Deprecated
@AllArgsConstructor
public class DefaultConvertService implements ConvertService, RequestedService {

    /** {@link DocumentManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private DocumentManager documentManager;

    /** {@link UrlManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private UrlManager urlManager;

    /** {@link RequestManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private RequestManager requestManager;

    /** {@link SettingsManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private SettingsManager settingsManager;

    /** {@link ObjectMapper}. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Deprecated
    @Override
    public ConvertResponse processConvert(final ConvertRequest convertRequest, final String fileId) throws Exception {
        String fileName = documentManager.getDocumentName(fileId);

        if (convertRequest.getFiletype() == null || convertRequest.getFiletype().isEmpty()) {
            convertRequest.setFiletype(documentManager.getExtension(fileName));
        }

        if (convertRequest.getKey() == null || convertRequest.getKey().isEmpty()) {
            convertRequest.setKey(documentManager.getDocumentKey(fileId, false));
        }

        if (convertRequest.getOutputtype() == null || convertRequest.getOutputtype().isEmpty()) {
            convertRequest.setOutputtype(documentManager.getDefaultConvertExtension(fileName));
        }

        if (convertRequest.getTitle() == null || convertRequest.getTitle().isEmpty()) {
            convertRequest.setTitle(
                    documentManager.getBaseName(fileName)
                    + "."
                    + convertRequest.getOutputtype()
            );
        }

        if (convertRequest.getUrl() == null || convertRequest.getUrl().isEmpty()) {
            convertRequest.setUrl(urlManager.getFileUrl(fileId));
        }

        if (Arrays.asList("bmp", "gif", "jpg", "png").contains(convertRequest.getOutputtype())
                && convertRequest.getThumbnail() == null) {

            Thumbnail thumbnail = Thumbnail.builder()
                    .first(false)
                    .build();

            convertRequest.setThumbnail(thumbnail);
        }

        Integer socketTimeout = (int) TimeUnit.SECONDS.toMillis(
                settingsManager.getDocsIntegrationSdkProperties()
                        .getDocumentServer()
                        .getConvertService()
                        .getSyncSocketTimeout()
        );

        HttpClientSettings httpClientSettings = null;

        if (convertRequest.getAsync() == null || !convertRequest.getAsync()) {
            httpClientSettings = HttpClientSettings.builder()
                    .socketTimeout(socketTimeout)
                    .build();
        }

        return requestManager.executePostRequest(this, convertRequest, httpClientSettings,
                new RequestManager.Callback<ConvertResponse>() {
                    public ConvertResponse doWork(final Object response) throws IOException {
                        String content = IOUtils.toString(((HttpEntity) response).getContent(), "utf-8");

                        ConvertResponse convertResponse = objectMapper.readValue(content.toString(),
                                ConvertResponse.class);

                        return convertResponse;
                    }
                });
    }
}
