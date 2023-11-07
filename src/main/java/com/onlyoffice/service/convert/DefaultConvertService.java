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

package com.onlyoffice.service.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.RequestableService;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.convertservice.ConvertResponse;
import com.onlyoffice.model.convertservice.convertrequest.Thumbnail;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import java.io.IOException;
import java.util.Arrays;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultConvertService implements ConvertService, RequestableService {
    private DocumentManager documentManager;
    private UrlManager urlManager;
    private RequestManager requestManager;

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

        return requestManager.executePostRequest(this, convertRequest,
                new RequestManager.Callback<ConvertResponse>() {
                    public ConvertResponse doWork(final HttpEntity httpEntity) throws IOException {
                        String content = IOUtils.toString(httpEntity.getContent(), "utf-8");

                        ObjectMapper objectMapper = new ObjectMapper();
                        ConvertResponse convertResponse = objectMapper.readValue(content.toString(),
                                ConvertResponse.class);

                        return convertResponse;
                    }
                });
    }
}
