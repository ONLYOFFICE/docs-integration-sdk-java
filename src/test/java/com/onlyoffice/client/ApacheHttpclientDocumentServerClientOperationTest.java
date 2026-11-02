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

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.model.commandservice.CommandRequest;
import com.onlyoffice.model.commandservice.commandrequest.Command;
import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.docbuilderservice.DocBuilderRequest;
import com.onlyoffice.model.settings.security.Security;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ApacheHttpclientDocumentServerClientOperationTest {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private ApacheHttpclientDocumentServerClient client;

    @BeforeEach
    void setup() {
        client = new ApacheHttpclientDocumentServerClient(
                DocumentServerClientSettings.builder()
                        .baseUrl("http://localhost")
                        .ignoreSSLCertificate(false)
                        .security(Security.builder()
                                .key("secret")
                                .header(HEADER)
                                .prefix(PREFIX)
                                .build()
                        )
                        .build()
        );
    }

    @Test
    public void whenConvert_thenTokensCarryConverterOperation() throws IOException {
        assertOperation(ConvertRequest.builder().key("key").url("http://localhost/file.docx").build(), "converter");
    }

    @Test
    public void whenCommand_thenTokensCarryCommandOperation() throws IOException {
        assertOperation(CommandRequest.builder().c(Command.VERSION).build(), "command");
    }

    @Test
    public void whenDocbuilder_thenTokensCarryDocbuilderOperation() throws IOException {
        assertOperation(DocBuilderRequest.builder().url("http://localhost/script.docbuilder").build(), "docbuilder");
    }

    @Test
    public void whenTokenIsSet_thenBodyTokenIsKept() throws IOException {
        ConvertRequest request = ConvertRequest.builder().key("key").token("preset").build();

        Assertions.assertEquals("preset", bodyToken(request));
    }

    private void assertOperation(final RequestEntity requestEntity, final String operation) throws IOException {
        ClassicHttpRequest request = ClassicRequestBuilder.post("http://localhost").build();
        client.authorizeRequest(request, requestEntity);

        String header = request.getFirstHeader(HEADER).getValue();
        Assertions.assertTrue(header.startsWith(PREFIX));
        Assertions.assertEquals(operation,
                JWT.decode(header.substring(PREFIX.length())).getClaim("operation").asString());

        Assertions.assertEquals(operation, JWT.decode(bodyToken(requestEntity)).getClaim("operation").asString());
    }

    private String bodyToken(final RequestEntity requestEntity) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        client.createEntity(requestEntity).writeTo(outputStream);

        return new ObjectMapper().readTree(outputStream.toByteArray()).get("token").asText();
    }
}
