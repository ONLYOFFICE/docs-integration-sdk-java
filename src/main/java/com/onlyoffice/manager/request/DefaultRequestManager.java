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

package com.onlyoffice.manager.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.common.RequestableService;
import com.onlyoffice.model.security.Credentials;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter(AccessLevel.PROTECTED)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRequestManager implements RequestManager {

    /** {@link UrlManager}. */
    private UrlManager urlManager;
    /** {@link JwtManager}. */
    private JwtManager jwtManager;
    /** {@link SettingsManager}. */
    private SettingsManager settingsManager;

        HttpGet request = new HttpGet(urlManager.replaceToInnerDocumentServerUrl(url));
    @Override
    public <R> R executePostRequest(final RequestableService requestableService, final RequestEntity requestEntity,
                                    final Callback<R> callback) throws Exception {
         Credentials credentials = Credentials.builder()
                 .key(settingsManager.getSecuritySecret())
                 .header(settingsManager.getSecurityHeader())
                 .prefix(settingsManager.getSecurityPrefix())
                 .build();

         String url = urlManager.getServiceUrl(requestableService);

         return executePostRequest(url, requestEntity, credentials, callback);
    }

    @Override
    public <R> R executePostRequest(final String url, final RequestEntity requestEntity, final Credentials credentials,
                                    final Callback<R> callback) throws Exception {
        HttpPost request = createPostRequest(url, requestEntity, credentials);

        return executeRequest(request, callback);
    }

    @Override
    public <R> R executeRequest(final HttpUriRequest request, final Callback<R> callback)
            throws Exception {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine == null) {
                    throw new ClientProtocolException(
                            settingsManager.getSDKSetting("integration-sdk.product.name")
                                    + " URL: " + request.getURI() + " did not return a response.\n"
                                    + "Request: " + request.toString() + "\n"
                                    + "Response: " + response
                    );
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity == null) {
                    throw new ClientProtocolException(
                            settingsManager.getSDKSetting("integration-sdk.product.name")
                                    + " URL: " + request.getURI() + " did not return content.\n"
                                    + "Request: " + request.toString() + "\n"
                                    + "Response: " + response
                    );
                }

                int statusCode = statusLine.getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    throw new ClientProtocolException(
                            settingsManager.getSDKSetting("integration-sdk.product.name")
                                    + " URL: " + request.getURI() + " return unexpected response.\n"
                                    + "Request: " + request.toString() + "\n"
                                    + "Response: " + response.toString()
                    );
                }

                R result = callback.doWork(resEntity);
                EntityUtils.consume(resEntity);

                return result;
            }
        }
    }

    private HttpPost createPostRequest(final String url, final RequestEntity requestEntity,
                                       final Credentials credentials) throws JsonProcessingException {
        HttpPost request = new HttpPost(url);

        if (credentials.getKey() != null && !credentials.getKey().isEmpty()) {
            Map<String, RequestEntity> payloadMap = new HashMap<>();
            payloadMap.put("payload", requestEntity);

            String headerToken = jwtManager.createToken(payloadMap, credentials.getKey());
            request.setHeader(credentials.getHeader(), credentials.getPrefix() + headerToken);

            String bodyToken = jwtManager.createToken(requestEntity, credentials.getKey());
            requestEntity.setToken(bodyToken);
        }

        ObjectMapper mapper = new ObjectMapper();
        StringEntity entity = new StringEntity(mapper.writeValueAsString(requestEntity), ContentType.APPLICATION_JSON);

        request.setEntity(entity);
        request.setHeader("Accept", "application/json");

        return request;
    }

    private CloseableHttpClient getHttpClient()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Integer timeout = (int) TimeUnit.SECONDS.toMillis(
                Long.parseLong(
                        settingsManager.getSDKSetting("integration-sdk.request.timeout")
                )
        );
        RequestConfig config = RequestConfig
                .custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();

        CloseableHttpClient httpClient;

        if (settingsManager.isIgnoreSSLCertificate()) {
            SSLContextBuilder builder = new SSLContextBuilder();

            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(final X509Certificate[] chain, final String authType)
                        throws CertificateException {
                    return true;
                }
            });

            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(builder.build(), new HostnameVerifier() {
                        @Override
                        public boolean verify(final String hostname, final SSLSession session) {
                            return true;
                        }
                    });

            httpClient = HttpClients
                    .custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setDefaultRequestConfig(config)
                    .build();
        } else {
            httpClient = HttpClientBuilder
                    .create()
                    .setDefaultRequestConfig(config)
                    .build();
        }

        return httpClient;
    }
}
