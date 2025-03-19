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

package com.onlyoffice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.exception.DocumentServerResponseException;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.commandservice.CommandRequest;
import com.onlyoffice.model.commandservice.CommandResponse;
import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.convertservice.ConvertRequest;
import com.onlyoffice.model.convertservice.ConvertResponse;

import com.onlyoffice.model.docbuilderservice.DocBuilderRequest;
import com.onlyoffice.model.docbuilderservice.DocBuilderResponse;
import com.onlyoffice.model.properties.docsintegrationsdk.HttpClientProperties;
import com.onlyoffice.utils.ConfigurationUtils;
import com.onlyoffice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class ApacheHttpclientDocumentServerClient extends AbstractDocumentServerClient {
    /** {@link HttpClient}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private CloseableHttpClient httpClient;
    /** {@link HttpClient}. */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private CloseableHttpClient httpClientForSyncConvertRequest;

    /** Base URL for document server API endpoints. */
    private String baseUrl;

    /** Flag indicating whether to ignore SSL certificate validation. */
    private boolean isIgnoreSSLCertificate;

    /** {@link ObjectMapper}. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs ApacheHttpclientDocumentServerClient with document server client settings.
     *
     * @param documentServerClientSettings the settings for the document server client
     */
    public ApacheHttpclientDocumentServerClient(final DocumentServerClientSettings documentServerClientSettings) {
        super(documentServerClientSettings);

        init();
    }

    /**
     * Constructs ApacheHttpclientDocumentServerClient with required managers.
     *
     * @param settingsManager the settings manager for managing configuration
     * @param urlManager the URL manager for handling document server URLs
     */
    public ApacheHttpclientDocumentServerClient(final SettingsManager settingsManager, final UrlManager urlManager) {
        super(settingsManager, urlManager);

        init();
    }

    @Override
    public Boolean healthcheck() {
        ClassicHttpRequest request = ClassicRequestBuilder.get(getBaseUrl())
                .setHeader("Upgrade", "")
                .setPath(ConfigurationUtils.getDocsIntegrationSdkProperties().getDocumentServer().getHealthCheckUrl())
                .build();

        return executeRequest(request, Boolean.class);
    }

    @Override
    public byte[] getFile(final String fileUrl) {
        URI uri;
        try {
            uri = new URI(fileUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ClassicHttpRequest request = ClassicRequestBuilder.get(getBaseUrl())
                .setPath(uri.getRawPath() + (uri.getRawQuery() != null ? "?" + uri.getRawQuery() : ""))
                .build();

        return executeRequest(request, byte[].class);
    }

    @Override
    public int getFile(final String fileUrl, final OutputStream outputStream) {
        URI uri;
        try {
            uri = new URI(fileUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ClassicHttpRequest request = ClassicRequestBuilder.get(getBaseUrl())
                .setPath(uri.getRawPath() + (uri.getRawQuery() != null ? "?" + uri.getRawQuery() : ""))
                .build();

        return executeRequest(request, outputStream);
    }

    @Override
    public ConvertResponse convert(final ConvertRequest convertRequest) {
        ClassicHttpRequest request = ClassicRequestBuilder.post(getBaseUrl())
                .setPath(ConfigurationUtils.getDocsIntegrationSdkProperties()
                        .getDocumentServer()
                        .getConvertService()
                        .getUrl()
                )
                .addParameter("shardkey", convertRequest.getKey())
                .setEntity(createEntity(convertRequest))
                .build();

        authorizeRequest(request, convertRequest);

        if (convertRequest.getAsync() != null && convertRequest.getAsync()) {
            return executeRequest(request, ConvertResponse.class);
        } else {
            return executeRequestWithHttpClientForSyncConvertRequest(request, ConvertResponse.class);
        }
    }

    @Override
    public CommandResponse command(final CommandRequest commandRequest) {
        ClassicHttpRequest request = ClassicRequestBuilder.post(getBaseUrl())
                .setPath(ConfigurationUtils.getDocsIntegrationSdkProperties()
                        .getDocumentServer()
                        .getCommandService()
                        .getUrl()
                )
                .addParameter("shardkey", commandRequest.getKey())
                .setEntity(createEntity(commandRequest))
                .build();

        authorizeRequest(request, commandRequest);

        return executeRequest(request, CommandResponse.class);
    }

    @Override
    public DocBuilderResponse docbuilder(final DocBuilderRequest docBuilderRequest) {
        ClassicHttpRequest request = ClassicRequestBuilder.post(getBaseUrl())
                .setPath(ConfigurationUtils.getDocsIntegrationSdkProperties()
                        .getDocumentServer()
                        .getDocbuilderService()
                        .getUrl()
                )
                .addParameter("shardkey", docBuilderRequest.getKey())
                .setEntity(createEntity(docBuilderRequest))
                .build();

        authorizeRequest(request, docBuilderRequest);

        return executeRequest(request, DocBuilderResponse.class);
    }

    protected <T> T executeRequestWithHttpClientForSyncConvertRequest(final ClassicHttpRequest classicHttpRequest,
                                                                      final Class<T> valueType) {
        prepareHttpClient();

        return executeRequest(this.httpClientForSyncConvertRequest, classicHttpRequest, inputStream -> {
            if (valueType.equals(byte[].class)) {
                return (T) IOUtils.toByteArray(inputStream);
            }

            return objectMapper.readValue(inputStream, valueType);
        });
    }

    protected <T> T executeRequest(final ClassicHttpRequest classicHttpRequest, final Class<T> valueType) {
        prepareHttpClient();

        return executeRequest(this.httpClient, classicHttpRequest, inputStream -> {
            if (valueType.equals(byte[].class)) {
                return (T) IOUtils.toByteArray(inputStream);
            }

            return objectMapper.readValue(inputStream, valueType);
        });
    }


    protected int executeRequest(final ClassicHttpRequest classicHttpRequest, final OutputStream outputStream) {
        prepareHttpClient();

        return executeRequest(this.httpClient, classicHttpRequest, inputStream -> {
            try {
                return IOUtils.copy(inputStream, outputStream);
            } finally {
                outputStream.close();
            }
        });
    }

    protected <R> R executeRequest(final HttpClient httpClient, final ClassicHttpRequest classicHttpRequest,
                                   final Callback<R> callback) {
        try {
            return httpClient.execute(classicHttpRequest, response -> {
                if (response.getCode() >= HttpStatus.SC_REDIRECTION) {
                    throw new ClientProtocolException(new StatusLine(response).toString());
                }

                final HttpEntity responseEntity = response.getEntity();
                if (responseEntity == null) {
                    return null;
                }

                try (InputStream inputStream = responseEntity.getContent()) {
                    return callback.doWork(inputStream);
                } finally {
                    EntityUtils.consume(responseEntity);
                }
            });
        } catch (IOException e) {
            throw new DocumentServerResponseException(
                    e,
                    classicHttpRequest.toString()
            );
        }
    }

    /**
     * Authorizes a request by adding a JWT header if security key is configured.
     *
     * @param request the HTTP request to authorize
     * @param requestEntity the request entity containing data for authorization
     */
    protected void authorizeRequest(final ClassicHttpRequest request, final RequestEntity requestEntity) {
        if (isSecurityEnabled()) {
            request.setHeader(
                    getSecurityHeader(),
                    SecurityUtils.createAuthorizationHeader(requestEntity, getSecurityKey(), getSecurityPrefix())
            );
        }
    }

    /**
     * Creates an HTTP entity from the request entity, generating auth token if needed.
     *
     * @param requestEntity the request entity to convert
     * @return HttpEntity containing the JSON payload
     */
    protected HttpEntity createEntity(final RequestEntity requestEntity) {
        return HttpEntities.create(outputStream -> {
                    if (requestEntity.getToken() == null && isSecurityEnabled()) {
                        requestEntity.setToken(
                                SecurityUtils.createToken(requestEntity, getSecurityKey())
                        );
                    }

                    objectMapper.writeValue(outputStream, requestEntity);
                    outputStream.flush();
                },
                ContentType.APPLICATION_JSON
        );
    }

    protected void prepareHttpClient() {
        if (shouldReinit()) {
            init();
        }
    }

    protected boolean shouldReinit() {
        HttpClientProperties httpClientProperties = getHttpClientProperties();

        return this.isIgnoreSSLCertificate != httpClientProperties.getIgnoreSslCertificate()
            || !this.baseUrl.equals(getBaseUrl());
    }

    protected void init() {
        HttpClientProperties httpClientProperties = getHttpClientProperties();
        this.isIgnoreSSLCertificate = httpClientProperties.getIgnoreSslCertificate();
        this.baseUrl = getBaseUrl();

        this.httpClient = createHttpClient(httpClientProperties);

        HttpClientProperties httpClientPropertiesForSyncConvertRequest = new HttpClientProperties(httpClientProperties);
        httpClientPropertiesForSyncConvertRequest.setSocketTimeout(getSyncSocketTimeoutForConvertService());

        this.httpClientForSyncConvertRequest = createHttpClient(httpClientPropertiesForSyncConvertRequest);
    }

    protected CloseableHttpClient createHttpClient(final HttpClientProperties httpClientProperties) {
        int connectionTimeout = (int) TimeUnit.SECONDS.toMillis(
                httpClientProperties.getConnectionTimeout()
        );

        int connectionRequestTimeout = (int) TimeUnit.SECONDS.toMillis(
                httpClientProperties.getConnectionRequestTimeout()
        );

        int socketTimeout = (int) TimeUnit.SECONDS.toMillis(
                httpClientProperties.getSocketTimeout()
        );

        PoolingHttpClientConnectionManagerBuilder poolingHttpClientConnectionManagerBuilder =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setDefaultConnectionConfig(ConnectionConfig.custom()
                                .setConnectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                                .setSocketTimeout(socketTimeout, TimeUnit.MILLISECONDS)
                                .build()
                        );

        if (Optional.ofNullable(httpClientProperties.getIgnoreSslCertificate()).orElse(false)) {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = null;
            try {
                sslContext = SSLContextBuilder
                        .create()
                        .loadTrustMaterial(acceptingTrustStrategy)
                        .build();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                throw new RuntimeException(e);
            }

            TlsSocketStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext);

            poolingHttpClientConnectionManagerBuilder.setTlsSocketStrategy(tlsStrategy);
        }

        return HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(connectionRequestTimeout, TimeUnit.MILLISECONDS)
                        .build()
                )
                .setConnectionManager(poolingHttpClientConnectionManagerBuilder.build())
                .build();
    }

    protected interface Callback<Result> {
        Result doWork(InputStream inputStream) throws IOException;
    }

    protected Long getSyncSocketTimeoutForConvertService() {
        return ConfigurationUtils.getDocsIntegrationSdkProperties()
                .getDocumentServer()
                .getConvertService()
                .getSyncSocketTimeout();
    }
}
