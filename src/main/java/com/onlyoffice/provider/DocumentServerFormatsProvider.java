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

package com.onlyoffice.provider;

import com.onlyoffice.client.DocumentServerClient;
import com.onlyoffice.model.common.Format;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class DocumentServerFormatsProvider implements FormatsProvider {
    /**
     * Default cache lifetime for document formats.
     */
    private static final Duration DEFAULT_CACHE_TTL = Duration.ofDays(1);

    /**
     * Default timeout before a failed cache refresh can be retried.
     */
    private static final Duration DEFAULT_CACHE_REFRESH_FAILURE_TIMEOUT = Duration.ofMinutes(1);

    /**
     * Cache lifetime for document formats.
     */
    private final Duration cacheTtl;

    /**
     * Timeout before a failed cache refresh can be retried.
     */
    private final Duration cacheRefreshFailureTimeout;

    /**
     * Time when the formats cache was last refreshed.
     */
    private Instant cacheTimestamp;

    /**
     * Time when the formats cache refresh last failed.
     */
    private Instant cacheRefreshFailureTimestamp;

    /**
     * Cached document formats.
     */
    private final List<Format> formats = new ArrayList<>();

    /**
     * Client used to retrieve formats from the Document Server.
     */
    private final DocumentServerClient documentServerClient;

    /**
     * Constructs a new provider with the default cache lifetime.
     *
     * @param documentServerClient The Document Server client.
     */
    public DocumentServerFormatsProvider(final DocumentServerClient documentServerClient) {
        this(documentServerClient, DEFAULT_CACHE_TTL, DEFAULT_CACHE_REFRESH_FAILURE_TIMEOUT);
    }

    DocumentServerFormatsProvider(final DocumentServerClient documentServerClient,
                                  final Duration cacheTtl,
                                  final Duration cacheRefreshFailureTimeout) {
        this.documentServerClient = documentServerClient;
        this.cacheTtl = cacheTtl;
        this.cacheRefreshFailureTimeout = cacheRefreshFailureTimeout;
    }

    @Override
    public synchronized List<Format> getFormats() {
        if (shouldRefreshCache()) {
            try {
                refreshCache();
                cacheRefreshFailureTimestamp = null;
            } catch (RuntimeException exception) {
                cacheRefreshFailureTimestamp = Instant.now();
            }
        }

        return new ArrayList<>(formats);
    }

    /**
     * Returns the list of document formats, refreshing the cache if it has expired.
     * Unlike {@link #getFormats()}, this method ignores the refresh failure timeout,
     * does not suppress errors, and does not update the failure timestamp.
     *
     * @return A list of supported formats.
     * @throws RuntimeException if the Document Server request fails.
     */
    public synchronized List<Format> getFormatsOrThrow() {
        if (isCacheExpired()) {
            refreshCache();
        }

        return new ArrayList<>(formats);
    }

    /**
     * Invalidates cached document formats.
     */
    public synchronized void invalidateCache() {
        formats.clear();
        cacheTimestamp = null;
        cacheRefreshFailureTimestamp = null;
    }

    private boolean shouldRefreshCache() {
        return isRefreshFailureTimeoutExpired() && isCacheExpired();
    }

    private boolean isRefreshFailureTimeoutExpired() {
        return cacheRefreshFailureTimestamp == null
                || !Instant.now().isBefore(cacheRefreshFailureTimestamp.plus(cacheRefreshFailureTimeout));
    }

    private boolean isCacheExpired() {
        return cacheTimestamp == null || Instant.now().isAfter(cacheTimestamp.plus(cacheTtl));
    }

    private void refreshCache() {
        List<Format> updatedFormats = documentServerClient.getFormats();
        formats.clear();
        formats.addAll(updatedFormats);
        cacheTimestamp = Instant.now();
    }
}
