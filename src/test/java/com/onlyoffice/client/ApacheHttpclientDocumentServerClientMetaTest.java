/**
 *
 * (c) Copyright Ascensio System SIA 2026
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

import com.onlyoffice.client.dto.ConfigResponse;
import com.onlyoffice.exception.DocumentServerResponseException;
import com.onlyoffice.manager.settings.DefaultSettingsManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import com.onlyoffice.model.common.Format;
import com.onlyoffice.model.documenteditor.config.document.DocumentType;
import com.onlyoffice.model.settings.SettingsConstants;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Checks the meta requests of the client against a local server: the endpoint, the method and how the answer
 * is read, with the client configured through the managers.
 */
public class ApacheHttpclientDocumentServerClientMetaTest {
    private static final String CONFIG_JSON = "{"
            + "\"authorization\":{\"header\":\"AuthorizationJwt\",\"prefix\":\"Bearer \"},"
            + "\"urls\":{"
            + "\"api\":\"/web-apps/apps/api/documents/api.js\","
            + "\"command\":\"/command\","
            + "\"converter\":\"/converter\","
            + "\"docbuilder\":\"/docbuilder\","
            + "\"newUrl\":\"/unknown\"},"
            + "\"limits\":{\"maxFileSize\":104857600},"
            + "\"langs\":[\"en\",\"de\"],"
            + "\"newField\":{\"nested\":true}"
            + "}";

    private static final String FORMATS_JSON = "["
            + "{\"name\":\"docx\",\"type\":\"word\",\"actions\":[\"view\",\"edit\"],"
            + "\"convert\":[\"pdf\",\"odt\"],"
            + "\"mime\":[\"application/vnd.openxmlformats-officedocument.wordprocessingml.document\"]},"
            + "{\"name\":\"xyz\",\"type\":\"future-editor\",\"actions\":[],\"convert\":[],\"mime\":[],"
            + "\"newField\":1}"
            + "]";

    private final Map<String, String> requests = new ConcurrentHashMap<>();

    private HttpServer server;

    private DocumentServerClient client;

    @BeforeEach
    void setup() throws IOException {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        respond("/meta/config", 200, CONFIG_JSON);
        respond("/meta/formats", 200, FORMATS_JSON);
        server.start();

        String url = "http://" + server.getAddress().getHostString() + ":" + server.getAddress().getPort();

        SettingsManager settingsManager = new DefaultSettingsManager() {
            @Override
            public String getSetting(final String name) {
                return SettingsConstants.URL.equals(name) ? url : "";
            }

            @Override
            public void setSetting(final String name, final String value) {
            }
        };

        client = new ApacheHttpclientDocumentServerClient(settingsManager, new DefaultUrlManager(settingsManager));
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    private void respond(final String path, final int status, final String body) {
        server.createContext(path, exchange -> {
            requests.put(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, bytes.length);

            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(bytes);
            }
        });
    }

    @Test
    public void whenGetConfig_thenRequestMetaConfigWithGet() {
        client.getConfig();

        Assertions.assertEquals(Collections.singletonMap("/meta/config", "GET"), requests);
    }

    @Test
    public void whenGetConfig_thenReadAllFieldsAndIgnoreUnknownOnes() {
        ConfigResponse config = client.getConfig();

        Assertions.assertEquals("AuthorizationJwt", config.getAuthorization().getHeader());
        Assertions.assertEquals("Bearer ", config.getAuthorization().getPrefix());
        Assertions.assertEquals("/web-apps/apps/api/documents/api.js", config.getUrls().getApi());
        Assertions.assertEquals("/command", config.getUrls().getCommand());
        Assertions.assertEquals("/converter", config.getUrls().getConverter());
        Assertions.assertEquals("/docbuilder", config.getUrls().getDocbuilder());
        Assertions.assertEquals(104857600L, config.getLimits().getMaxFileSize());
        Assertions.assertEquals(Arrays.asList("en", "de"), config.getLangs());
    }

    @Test
    public void whenGetFormats_thenRequestMetaFormatsWithGet() {
        client.getFormats();

        Assertions.assertEquals(Collections.singletonMap("/meta/formats", "GET"), requests);
    }

    @Test
    public void whenGetFormats_thenReadAllFields() {
        List<Format> formats = client.getFormats();

        Assertions.assertEquals(2, formats.size());

        Format docx = formats.get(0);
        Assertions.assertEquals("docx", docx.getName());
        Assertions.assertEquals(DocumentType.WORD, docx.getType());
        Assertions.assertEquals(Arrays.asList("view", "edit"), docx.getActions());
        Assertions.assertEquals(Arrays.asList("pdf", "odt"), docx.getConvert());
        Assertions.assertEquals(
                Collections.singletonList("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
                docx.getMime()
        );
    }

    @Test
    public void whenGetFormatsWithUnknownTypeAndFields_thenReadTypeAsNull() {
        Format xyz = client.getFormats().get(1);

        Assertions.assertEquals("xyz", xyz.getName());
        Assertions.assertNull(xyz.getType());
    }

    @Test
    public void whenServerAnswersError_thenThrowDocumentServerResponseException() {
        server.removeContext("/meta/formats");
        respond("/meta/formats", 500, "{}");

        Assertions.assertThrows(DocumentServerResponseException.class, () -> client.getFormats());
    }
}
