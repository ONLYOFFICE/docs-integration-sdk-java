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

import com.onlyoffice.cache.DocumentServerMetaCache;
import com.onlyoffice.client.DocumentServerClient;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.Format;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Supplies the formats of the Document Server ({@code /meta/formats}), kept in a {@link DocumentServerMetaCache}.
 *
 * <p>The cache is bound to the address of the Document Server: when the address changes, the cached formats are
 * dropped and loaded again from the new server. The address is read on every call, so a change made on another
 * node of a cluster is seen too.
 *
 * <p>While no formats are loaded, such as during the first load, while the Document Server is down or when no
 * address is set, the formats of the fallback provider are returned, by default the ones bundled with the SDK
 * ({@link DefaultFormatsProvider}).
 *
 * <p>The returned list can't be changed, but its {@link Format} objects are shared by every caller: don't change
 * them.
 */
public class DocumentServerFormatsProvider implements FormatsProvider {
    /**
     * Cache of the formats of the Document Server.
     */
    private final DocumentServerMetaCache<List<Format>> cache;

    /**
     * Supplies the address of the Document Server the client sends requests to.
     */
    private final Supplier<String> documentServerUrl;

    /**
     * Supplies the formats while none are loaded.
     */
    private final FormatsProvider fallback;

    /**
     * Called with the errors of failed loads, {@code null} if none.
     */
    private final Consumer<? super Exception> onError;

    /**
     * Address of the Document Server the cached formats belong to.
     */
    private final AtomicReference<String> cachedFor = new AtomicReference<>();

    /**
     * Constructs a provider with the default settings: the address is taken from
     * {@link UrlManager#getInnerDocumentServerUrl()}, the formats are fresh for a day, and the bundled formats are
     * returned while none are loaded.
     *
     * @param documentServerClient The Document Server client.
     * @param urlManager The URL manager the client takes the address of the Document Server from.
     */
    public DocumentServerFormatsProvider(final DocumentServerClient documentServerClient,
                                         final UrlManager urlManager) {
        this(builder(documentServerClient, urlManager::getInnerDocumentServerUrl));
    }

    private DocumentServerFormatsProvider(final Builder builder) {
        this.documentServerUrl = builder.documentServerUrl;
        this.fallback = builder.fallback;
        this.onError = builder.onError;
        this.cache = builder.cache.onError(builder.onError).build();
    }

    /**
     * Starts building a provider.
     *
     * @param documentServerClient The Document Server client.
     * @param documentServerUrl Supplies the address of the Document Server the client sends requests to, such as
     *                          {@code urlManager::getInnerDocumentServerUrl}. Read on every call: when it changes,
     *                          the formats are loaded again. A {@code null} or empty address means that none is set.
     * @return A builder.
     */
    public static Builder builder(final DocumentServerClient documentServerClient,
                                  final Supplier<String> documentServerUrl) {
        return new Builder(documentServerClient, documentServerUrl);
    }

    /**
     * Returns the formats of the Document Server, loading them when they are stale, missing or belong to another
     * address. Returns the formats of the fallback provider while none are loaded. Doesn't throw when the
     * Document Server is unavailable, or when the address supplier or the fallback provider throws an
     * {@link RuntimeException}: the exception goes to {@code onError}, and when the fallback provider fails,
     * an empty list is returned.
     *
     * @return An unmodifiable list of supported formats.
     */
    @Override
    public List<Format> getFormats() {
        String url;

        try {
            url = documentServerUrl.get();
        } catch (RuntimeException e) {
            report(e);

            return fallbackFormats();
        }

        if (url == null || url.isEmpty()) {
            return fallbackFormats();
        }

        String previous = cachedFor.get();

        if (!url.equals(previous) && cachedFor.compareAndSet(previous, url)) {
            cache.invalidate();
        }

        List<Format> formats = cache.get();

        return formats != null ? formats : fallbackFormats();
    }

    /**
     * Drops the cached formats, so that the next call to {@link #getFormats()} loads them again. A change of the
     * address of the Document Server is detected without it.
     */
    public void invalidateCache() {
        cache.invalidate();
    }

    private List<Format> fallbackFormats() {
        try {
            List<Format> formats = fallback.getFormats();

            return formats != null ? formats : Collections.emptyList();
        } catch (RuntimeException e) {
            report(e);

            return Collections.emptyList();
        }
    }

    private void report(final Exception error) {
        if (onError == null) {
            return;
        }

        try {
            onError.accept(error);
        } catch (RuntimeException ignored) {
            // an error of the handler must not break getFormats()
        }
    }

    /**
     * Builds a {@link DocumentServerFormatsProvider}.
     */
    public static final class Builder {
        /**
         * Builder of the cache of the formats.
         */
        private final DocumentServerMetaCache.Builder<List<Format>> cache;

        /**
         * Supplies the address of the Document Server.
         */
        private final Supplier<String> documentServerUrl;

        /**
         * Supplies the formats while none are loaded.
         */
        private FormatsProvider fallback = new DefaultFormatsProvider();

        /**
         * Called with the errors of failed loads.
         */
        private Consumer<? super Exception> onError;

        private Builder(final DocumentServerClient documentServerClient, final Supplier<String> documentServerUrl) {
            Objects.requireNonNull(documentServerClient, "documentServerClient");

            this.documentServerUrl = Objects.requireNonNull(documentServerUrl, "documentServerUrl");
            this.cache = DocumentServerMetaCache.builder(
                    () -> Collections.unmodifiableList(new ArrayList<>(documentServerClient.getFormats()))
            );
        }

        /**
         * Sets the provider of the formats returned while none are loaded. Default: {@link DefaultFormatsProvider},
         * the formats bundled with the SDK.
         *
         * @param value The fallback provider.
         * @return This builder.
         */
        public Builder fallback(final FormatsProvider value) {
            this.fallback = Objects.requireNonNull(value, "fallback");

            return this;
        }

        /**
         * Sets the handler of the errors of failed loads, such as {@code e -> log.warn("...", e)}. An exception it
         * throws is ignored.
         *
         * @param value The handler, or {@code null} for none.
         * @return This builder.
         */
        public Builder onError(final Consumer<? super Exception> value) {
            this.onError = value;

            return this;
        }

        /**
         * Sets how long the loaded formats are fresh. Default: a day.
         *
         * @param value A positive duration.
         * @return This builder.
         * @see DocumentServerMetaCache.Builder#ttl(Duration)
         */
        public Builder ttl(final Duration value) {
            cache.ttl(value);

            return this;
        }

        /**
         * Sets how long to wait after a failed load before another. Default: a minute.
         *
         * @param value A duration that is not negative.
         * @return This builder.
         * @see DocumentServerMetaCache.Builder#retry(Duration)
         */
        public Builder retry(final Duration value) {
            cache.retry(value);

            return this;
        }

        /**
         * Sets how long a load may run before it counts as failed. Default: a minute.
         *
         * @param value A positive duration.
         * @return This builder.
         * @see DocumentServerMetaCache.Builder#loadTimeout(Duration)
         */
        public Builder loadTimeout(final Duration value) {
            cache.loadTimeout(value);

            return this;
        }

        /**
         * Builds the provider.
         *
         * @return A new provider with no formats loaded.
         */
        public DocumentServerFormatsProvider build() {
            return new DocumentServerFormatsProvider(this);
        }
    }
}
