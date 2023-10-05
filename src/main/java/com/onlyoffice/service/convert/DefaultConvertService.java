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
import com.onlyoffice.model.convert.Convert;
import com.onlyoffice.model.convert.thumbnail.Thumbnail;
import com.onlyoffice.model.service.Service;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultConvertService implements ConvertService {
    private DocumentManager documentManager;
    private UrlManager urlManager;
    private RequestManager requestManager;

    public JSONObject processConvert(final Convert convert, final String fileId) throws Exception {
        String fileName = documentManager.getDocumentName(fileId);

        if (convert.getFiletype() == null || convert.getFiletype().isEmpty()) {
            convert.setFiletype(documentManager.getExtension(fileName));
        }

        if (convert.getKey() == null || convert.getKey().isEmpty()) {
            convert.setKey(documentManager.getDocumentKey(fileId, false));
        }

        if (convert.getOutputtype() == null || convert.getOutputtype().isEmpty()) {
            convert.setOutputtype(documentManager.getDefaultConvertExtension(fileName));
        }

        if (convert.getTitle() == null || convert.getTitle().isEmpty()) {
            convert.setTitle(
                    documentManager.geBaseName(fileName)
                    + "."
                    + documentManager.getDefaultConvertExtension(fileName)
            );
        }

        if (convert.getUrl() == null || convert.getUrl().isEmpty()) {
            convert.setUrl(urlManager.getFileUrl(fileId));
        }

        if (Arrays.asList("bmp", "gif", "jpg", "png").contains(convert.getOutputtype())
                && convert.getThumbnail() == null) {

            Thumbnail thumbnail = Thumbnail.builder()
                    .first(false)
                    .build();

            convert.setThumbnail(thumbnail);
        }

        ObjectMapper mapper = new ObjectMapper();
        JSONObject bodyJson = new JSONObject(mapper.writeValueAsString(convert));

        return requestManager.executePostRequest(Service.CONVERT_SERVICE, bodyJson,
                new RequestManager.Callback<JSONObject>() {
                    public JSONObject doWork(final HttpEntity httpEntity) throws IOException {
                        String content = IOUtils.toString(httpEntity.getContent(), "utf-8");

                        JSONObject convertResponse = new JSONObject(content);

                        return convertResponse;
                    }
                });
    }
}
