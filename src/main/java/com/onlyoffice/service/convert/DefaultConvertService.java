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

import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.url.UrlManager;
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

    public JSONObject convert(String fileId, String region, boolean async) throws Exception {
        String fileName = documentManager.getDocumentName(fileId);
        String key = documentManager.getDocumentKey(fileId, false);
        String currentExtension = documentManager.getExtension(fileName);
        String targetExtension = documentManager.getDefaultConvertExtension(fileName);
        String url = urlManager.getFileUrl(fileId);

        return convert(key, currentExtension, targetExtension, url, region, null, async);
    }

    public JSONObject convert(String fileId, String targetExtension, String region, String title, boolean async) throws Exception {
        String fileName = documentManager.getDocumentName(fileId);
        String key = documentManager.getDocumentKey(fileId, false);
        String currentExtension = documentManager.getExtension(fileName);
        String url = urlManager.getFileUrl(fileId);

        return convert(key, currentExtension, targetExtension, url, region, title, async);
    }

    public JSONObject convert(String key, String currentExtension, String targetExtension, String url, String region, String title, boolean async) throws Exception {
        JSONObject body = new JSONObject();
        body.put("async", async);
        body.put("embeddedfonts", true);
        body.put("filetype", currentExtension);
        body.put("outputtype", targetExtension);
        body.put("key", key);
        body.put("url", url);
        body.put("region", region);
        body.put("title", title);

        if (Arrays.asList("bmp", "gif", "jpg", "png").contains(targetExtension)) {
            JSONObject thumbnail = new JSONObject();
            thumbnail.put("first", false);
            body.put("thumbnail", thumbnail);
        }

        return requestManager.executePostRequest(Service.CONVERT_SERVICE, body,
                new RequestManager.Callback<JSONObject>() {
                    public JSONObject doWork(HttpEntity httpEntity) throws IOException {
                        String content = IOUtils.toString(httpEntity.getContent(), "utf-8");

                        JSONObject convertResponse = new JSONObject(content);

                        return convertResponse;
                    }
                });
    }
}
