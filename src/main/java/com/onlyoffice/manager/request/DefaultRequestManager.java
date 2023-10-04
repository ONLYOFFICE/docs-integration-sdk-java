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

import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.service.Service;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRequestManager implements RequestManager {
    protected UrlManager urlManager;
    protected JwtManager jwtManager;
    protected SettingsManager settingsManager;

    public <R> R executeGetRequest(String url, Callback<R> callback) throws Exception {
        HttpGet request = new HttpGet(urlManager.replaceToInnerDocumentServerUrl(url));

        return executeRequest(Service.DOCUMENT_SERVER, request, callback);
    }

    public <R> R executePostRequest(Service service, JSONObject data,
                                    Callback<R> callback) throws Exception {
        String url = urlManager.getInnerDocumentServerUrl();
        String secretKey = settingsManager.getSecuritySecret();
        String jwtHeader = settingsManager.getSecurityHeader();
        String jwtPrefix = settingsManager.getSecurityPrefix();

        return executePostRequest(service, data, url, secretKey, jwtHeader, jwtPrefix, callback);
    }

    public <R> R executePostRequest(Service service, JSONObject data, String url, String secretKey, String jwtHeader,
                                    String jwtPrefix, Callback<R> callback) throws Exception {
        HttpPost request = new HttpPost(urlManager.sanitizeUrl(url) + service.path);

        if (secretKey != null && !secretKey.isEmpty()) {
            String token = jwtManager.createToken(data, secretKey);

            JSONObject payloadBody = new JSONObject();
            payloadBody.put("payload", data);

            String headerToken = jwtManager.createToken(data, secretKey);

            data.put("token", token);
            request.setHeader(jwtHeader, jwtPrefix + headerToken);
        }

        StringEntity requestEntity = new StringEntity(data.toString(), ContentType.APPLICATION_JSON);

        request.setEntity(requestEntity);
        request.setHeader("Accept", "application/json");

        return executeRequest(service, request, callback);
    }

    public <R> R executeRequest(Service service, HttpUriRequest request, Callback<R> callback)
            throws Exception {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine == null) {
                    throw new ClientProtocolException(
                            settingsManager.getSDKSetting("integration-sdk.product.name")
                                    + " " + service.name() + " did not return a response.\n"
                                    + "Request: " + request.toString() + "\n"
                                    + "Response: " + response
                    );
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity == null) {
                    throw new ClientProtocolException(
                            settingsManager.getSDKSetting("integration-sdk.product.name")
                                    + " " + service.name() + " did not return content.\n"
                                    + "Request: " + request.toString() + "\n"
                                    + "Response: " + response
                    );
                }

                int statusCode = statusLine.getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    throw new ClientProtocolException(
                            settingsManager.getSDKSetting("integration-sdk.product.name")
                                    + " " + service.name() + " return unexpected response.\n"
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
