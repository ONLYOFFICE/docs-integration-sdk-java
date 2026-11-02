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

package com.onlyoffice.cache;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.LongSupplier;


/**
 * Keeps one answer of the Document Server in memory, such as {@code /meta/config} or {@code /meta/formats},
 * so that a request to your application doesn't cost one to the Document Server. Create one cache per answer.
 *
 * <p>A call to {@link #get()} that finds the data stale or missing starts a load on its own thread and waits
 * for it. Calls made while a load runs don't wait: they return what is cached, or {@code null} before the first
 * load succeeds. A failed load keeps the old data and is not retried for {@code retry}. A load still running
 * after {@code loadTimeout} counts as failed: its result is ignored, and the next call may start another load.
 *
 * <p>The cache sends no requests of its own and starts no threads: loads start only from {@link #get()}.
 * It is safe to use from many threads.
 *
 * <pre>{@code
 * DocumentServerMetaCache<List<Format>> formats = DocumentServerMetaCache
 *         .builder(documentServerClient::getFormats)
 *         .onError(e -> log.warn("Document Server formats", e))
 *         .build();
 *
 * List<Format> list = formats.get(); // null while nothing has loaded
 * }</pre>
 *
 * @param <T> The type of the cached data.
 */
public final class DocumentServerMetaCache<T> {
    /**
     * Default time the loaded data is fresh.
     */
    public static final Duration DEFAULT_TTL = Duration.ofDays(1);

    /**
     * Default time to wait after a failed load before another.
     */
    public static final Duration DEFAULT_RETRY = Duration.ofMinutes(1);

    /**
     * Default time a load may run before it counts as failed.
     */
    public static final Duration DEFAULT_LOAD_TIMEOUT = Duration.ofMinutes(1);

    /**
     * Loads the data.
     */
    private final Callable<? extends T> loader;

    /**
     * Time the loaded data is fresh, in nanoseconds.
     */
    private final long ttlNanos;

    /**
     * Time to wait after a failed load before another, in nanoseconds.
     */
    private final long retryNanos;

    /**
     * Time a load may run before it counts as failed, in nanoseconds.
     */
    private final long loadTimeoutNanos;

    /**
     * Called with the error of a failed load, {@code null} if none.
     */
    private final Consumer<? super Exception> onError;

    /**
     * Monotonic clock in nanoseconds.
     */
    private final LongSupplier clock;

    /**
     * Guards the state below. Never held while the data loads or {@code onError} runs.
     */
    private final Object lock = new Object();

    /**
     * Cached data, {@code null} while nothing has loaded.
     */
    private T data;

    /**
     * Time the cached data was loaded.
     */
    private long loadedAt;

    /**
     * Whether the last load failed and the next one waits for {@link #retryNanos}.
     */
    private boolean failed;

    /**
     * Time the last load failed.
     */
    private long failedAt;

    /**
     * Generation of the current load; a load whose generation is no longer current is ignored.
     */
    private long loadId;

    /**
     * Whether a load runs.
     */
    private boolean loading;

    /**
     * Time the running load started.
     */
    private long loadStartedAt;

    private DocumentServerMetaCache(final Builder<T> builder) {
        this.loader = builder.loader;
        this.ttlNanos = toNanos(builder.ttl);
        this.retryNanos = toNanos(builder.retry);
        this.loadTimeoutNanos = toNanos(builder.loadTimeout);
        this.onError = builder.onError;
        this.clock = builder.clock;
    }

    /**
     * Starts building a cache.
     *
     * @param loader Loads the data, such as {@code documentServerClient::getFormats}. Called whenever
     *               {@link #get()} finds the data stale or missing. Returns an object, never {@code null}.
     *               Should give up on its own, for example with the timeouts of the HTTP client: the cache
     *               can't interrupt it.
     * @param <T> The type of the cached data.
     * @return A builder.
     */
    public static <T> Builder<T> builder(final Callable<? extends T> loader) {
        return new Builder<>(loader);
    }

    /**
     * Returns the cached data, or {@code null} while nothing has loaded yet. When the data is stale or missing,
     * no load runs and the last one didn't fail less than {@code retry} ago, loads the data on the calling thread
     * and returns once the load ends: the new data, or, when the load fails, the old. Otherwise returns at once.
     * Never throws because of a failed load.
     *
     * <p>Every call returns the same object, so a change to it is seen by every other caller. Don't change it.
     *
     * @return The cached data, or {@code null} while nothing has loaded yet.
     */
    public T get() {
        T current;
        long id = 0;
        boolean start = false;
        Exception timeout = null;

        synchronized (lock) {
            long now = clock.getAsLong();

            if (loading) {
                if (now - loadStartedAt < loadTimeoutNanos) {
                    return data;
                }

                loadId++;
                loading = false;
                timeout = timeOut(now);
            }

            current = data;

            if (shouldLoad(now)) {
                loadId++;
                id = loadId;
                loading = true;
                loadStartedAt = now;
                start = true;
            }
        }

        report(timeout);

        return start ? load(id) : current;
    }

    /**
     * Drops the cached data and ignores the result of a load that runs, so that the next call to {@link #get()}
     * loads the data again at once. Call it when the answer may have changed, such as when the address of the
     * Document Server changes.
     */
    public void invalidate() {
        synchronized (lock) {
            loadId++;
            loading = false;
            data = null;
            failed = false;
        }
    }

    private T load(final long id) {
        T loaded = null;
        Exception error = null;
        boolean ended = false;

        try {
            loaded = loader.call();

            if (loaded == null) {
                error = new NullPointerException("The loader of the cache returned null");
            }

            ended = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            error = e;
            ended = true;
        } catch (Exception e) {
            error = e;
            ended = true;
        } finally {
            if (!ended) {
                end(id);
            }
        }

        T current;
        Exception report = null;

        synchronized (lock) {
            if (id != loadId) {
                return data;
            }

            long now = clock.getAsLong();
            loading = false;

            if (now - loadStartedAt >= loadTimeoutNanos) {
                report = timeOut(now);
            } else if (error != null) {
                failed = true;
                failedAt = now;
                report = error;
            } else {
                data = loaded;
                loadedAt = now;
                failed = false;
            }

            current = data;
        }

        report(report);

        return current;
    }

    /**
     * Ends a load that threw an {@link Error}, so that the next call doesn't wait for it.
     *
     * @param id The generation of the load.
     */
    private void end(final long id) {
        synchronized (lock) {
            if (id == loadId) {
                loading = false;
                failed = true;
                failedAt = clock.getAsLong();
            }
        }
    }

    private boolean shouldLoad(final long now) {
        boolean stale = data == null || now - loadedAt >= ttlNanos;
        boolean retryAllowed = !failed || now - failedAt >= retryNanos;

        return stale && retryAllowed;
    }

    private Exception timeOut(final long now) {
        failed = true;
        failedAt = now;

        return new TimeoutException("The load did not end within " + Duration.ofNanos(loadTimeoutNanos));
    }

    private void report(final Exception error) {
        if (error == null || onError == null) {
            return;
        }

        try {
            onError.accept(error);
        } catch (RuntimeException ignored) {
            // an error of the handler must not break get()
        }
    }

    private static long toNanos(final Duration duration) {
        try {
            return duration.toNanos();
        } catch (ArithmeticException e) {
            return Long.MAX_VALUE;
        }
    }

    /**
     * Builds a {@link DocumentServerMetaCache}.
     *
     * @param <T> The type of the cached data.
     */
    public static final class Builder<T> {
        /**
         * Loads the data.
         */
        private final Callable<? extends T> loader;

        /**
         * Time the loaded data is fresh.
         */
        private Duration ttl = DEFAULT_TTL;

        /**
         * Time to wait after a failed load before another.
         */
        private Duration retry = DEFAULT_RETRY;

        /**
         * Time a load may run before it counts as failed.
         */
        private Duration loadTimeout = DEFAULT_LOAD_TIMEOUT;

        /**
         * Called with the error of a failed load.
         */
        private Consumer<? super Exception> onError;

        /**
         * Monotonic clock in nanoseconds.
         */
        private LongSupplier clock = System::nanoTime;

        private Builder(final Callable<? extends T> loader) {
            this.loader = Objects.requireNonNull(loader, "loader");
        }

        /**
         * Sets how long the loaded data is fresh. Default: a day.
         *
         * @param value A positive duration.
         * @return This builder.
         * @throws IllegalArgumentException when the duration is not positive.
         */
        public Builder<T> ttl(final Duration value) {
            this.ttl = requirePositive("ttl", value);

            return this;
        }

        /**
         * Sets how long to wait after a failed load before {@link DocumentServerMetaCache#get()} starts another.
         * Default: a minute.
         *
         * @param value A duration that is not negative.
         * @return This builder.
         * @throws IllegalArgumentException when the duration is negative.
         */
        public Builder<T> retry(final Duration value) {
            Objects.requireNonNull(value, "retry");

            if (value.isNegative()) {
                throw new IllegalArgumentException("retry must not be negative, got: " + value);
            }

            this.retry = value;

            return this;
        }

        /**
         * Sets how long a load may run. A load that hasn't ended by then counts as failed: a
         * {@link TimeoutException} goes to {@code onError}, and what the load ends with later is ignored.
         * Default: a minute.
         *
         * @param value A positive duration.
         * @return This builder.
         * @throws IllegalArgumentException when the duration is not positive.
         */
        public Builder<T> loadTimeout(final Duration value) {
            this.loadTimeout = requirePositive("loadTimeout", value);

            return this;
        }

        /**
         * Sets the handler of the errors of failed loads. An exception it throws is ignored.
         *
         * @param value The handler, or {@code null} for none.
         * @return This builder.
         */
        public Builder<T> onError(final Consumer<? super Exception> value) {
            this.onError = value;

            return this;
        }

        Builder<T> clock(final LongSupplier value) {
            this.clock = Objects.requireNonNull(value, "clock");

            return this;
        }

        /**
         * Builds the cache.
         *
         * @return A new cache with nothing loaded.
         */
        public DocumentServerMetaCache<T> build() {
            return new DocumentServerMetaCache<>(this);
        }

        private static Duration requirePositive(final String name, final Duration value) {
            Objects.requireNonNull(value, name);

            if (value.isZero() || value.isNegative()) {
                throw new IllegalArgumentException(name + " must be positive, got: " + value);
            }

            return value;
        }
    }
}
