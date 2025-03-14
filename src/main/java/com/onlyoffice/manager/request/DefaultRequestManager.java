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

package com.onlyoffice.manager.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.common.RequestedService;
import com.onlyoffice.model.settings.HttpClientSettings;
import com.onlyoffice.model.settings.security.Security;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Deprecated
@AllArgsConstructor
public class DefaultRequestManager implements RequestManager {

    /** {@link UrlManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private UrlManager urlManager;

    /** {@link JwtManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private JwtManager jwtManager;

    /** {@link SettingsManager}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private SettingsManager settingsManager;

    /** {@link ObjectMapper}. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Deprecated
    @Override
    public <R> R executeGetRequest(final String url, final Callback<R> callback) throws Exception {
        HttpClientSettings httpClientSettings = HttpClientSettings.builder()
                .ignoreSSLCertificate(settingsManager.isIgnoreSSLCertificate())
                .build();

        return executeGetRequest(url, httpClientSettings, callback);
    }

    @Deprecated
    @Override
    public <R> R executeGetRequest(final String url, final HttpClientSettings httpClientSettings,
                                   final Callback<R> callback)
            throws Exception {
        HttpGet httpGet = new HttpGet(url);

        return executeRequest(httpGet, httpClientSettings, callback);
    }

    @Deprecated
    @Override
    public <R> R executePostRequest(final RequestedService requestedService, final RequestEntity requestEntity,
                                    final Callback<R> callback) throws Exception {
         Security security = Security.builder()
                 .key(settingsManager.getSecurityKey())
                 .header(settingsManager.getSecurityHeader())
                 .prefix(settingsManager.getSecurityPrefix())
                 .build();

         String url = urlManager.getServiceUrl(requestedService);

         return executePostRequest(url, requestEntity, security, null, callback);
    }

    @Deprecated
    @Override
    public <R> R executePostRequest(final RequestedService requestedService, final RequestEntity requestEntity,
                                    final HttpClientSettings httpClientSettings, final Callback<R> callback)
            throws Exception {
        Security security = Security.builder()
                .key(settingsManager.getSecurityKey())
                .header(settingsManager.getSecurityHeader())
                .prefix(settingsManager.getSecurityPrefix())
                .build();

        String url = urlManager.getServiceUrl(requestedService);

        return executePostRequest(url, requestEntity, security, httpClientSettings, callback);
    }

    @Deprecated
    @Override
    public <R> R executePostRequest(final String url, final RequestEntity requestEntity, final Security security,
                                    final HttpClientSettings httpClientSettings, final Callback<R> callback)
            throws Exception {
        HttpPost request = createPostRequest(url, requestEntity, security);

        return executeRequest(request, httpClientSettings, callback);
    }

    private <R> R executeRequest(final HttpUriRequest request, final HttpClientSettings httpClientSettings,
                                 final Callback<R> callback)
            throws Exception {
        try (CloseableHttpClient httpClient = getHttpClient(httpClientSettings)) {
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity == null) {
                    throw new ClientProtocolException(
                            settingsManager.getDocsIntegrationSdkProperties().getProduct().getName()
                                    + " URL: " + request.getUri() + " did not return content.\n"
                                    + "Request: " + request.toString() + "\n"
                                    + "Response: " + response
                    );
                }

                int statusCode = response.getCode();
                if (statusCode != HttpStatus.SC_OK) {
                    throw new ClientProtocolException(
                            settingsManager.getDocsIntegrationSdkProperties().getProduct().getName()
                                    + " URL: " + request.getUri() + " return unexpected response.\n"
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
                                       final Security security) throws JsonProcessingException, URISyntaxException {
        URI uri = URI.create(url);
        if (requestEntity.getKey() != null && !requestEntity.getKey().isEmpty()) {
            uri = new URIBuilder(url).addParameter("shardkey", requestEntity.getKey()).build();
        }

        HttpPost request = new HttpPost(uri);

        if (security.getKey() != null && !security.getKey().isEmpty()) {
            Map<String, RequestEntity> payloadMap = new HashMap<>();
            payloadMap.put("payload", requestEntity);

            String headerToken = jwtManager.createToken(
                    objectMapper.convertValue(payloadMap, Map.class),
                    security.getKey()
            );
            request.setHeader(security.getHeader(), security.getPrefix() + headerToken);

            String bodyToken = jwtManager.createToken(requestEntity, security.getKey());
            requestEntity.setToken(bodyToken);
        }

        StringEntity entity = new StringEntity(
                objectMapper.writeValueAsString(requestEntity),
                ContentType.APPLICATION_JSON
        );

        request.setEntity(entity);
        request.setHeader("Accept", "application/json");

        return request;
    }

    private CloseableHttpClient getHttpClient(final HttpClientSettings httpClientSettings)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Boolean ignoreSSLCertificate = settingsManager.isIgnoreSSLCertificate();

        Integer connectionTimeout = (int) TimeUnit.SECONDS.toMillis(
                settingsManager.getDocsIntegrationSdkProperties()
                        .getHttpClient()
                        .getConnectionTimeout()
        );

        Integer connectionRequestTimeout = (int) TimeUnit.SECONDS.toMillis(
                settingsManager.getDocsIntegrationSdkProperties()
                        .getHttpClient()
                        .getConnectionRequestTimeout()
        );

        Integer socketTimeout = (int) TimeUnit.SECONDS.toMillis(
                settingsManager.getDocsIntegrationSdkProperties()
                        .getHttpClient()
                        .getSocketTimeout()
        );

        if (httpClientSettings != null) {
            if (httpClientSettings.getConnectionTimeout() != null) {
                connectionTimeout = httpClientSettings.getConnectionTimeout();
            }

            if (httpClientSettings.getConnectionRequestTimeout() != null) {
                connectionRequestTimeout = httpClientSettings.getConnectionRequestTimeout();
            }


            if (httpClientSettings.getSocketTimeout() != null) {
                socketTimeout = httpClientSettings.getSocketTimeout();
            }

            if (httpClientSettings.getIgnoreSSLCertificate() != null) {
                ignoreSSLCertificate = httpClientSettings.getIgnoreSSLCertificate();
            }
        }


        PoolingHttpClientConnectionManagerBuilder poolingHttpClientConnectionManagerBuilder =
                PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setConnectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                        .setSocketTimeout(socketTimeout, TimeUnit.MILLISECONDS)
                        .build()
                );

        if (ignoreSSLCertificate) {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial(acceptingTrustStrategy)
                    .build();
            TlsSocketStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext);

            poolingHttpClientConnectionManagerBuilder.setTlsSocketStrategy(tlsStrategy);
        }

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(connectionRequestTimeout, TimeUnit.MILLISECONDS)
                        .build()
                )
                .setConnectionManager(poolingHttpClientConnectionManagerBuilder.build())
                .build();
    }
}
