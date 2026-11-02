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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentServerMetaCacheTest {
    private static final long WAIT_SECONDS = 5;

    private final AtomicLong clock = new AtomicLong(-1_000_000L);

    private final List<Exception> errors = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService executor;

    @BeforeEach
    void setup() {
        executor = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    private DocumentServerMetaCache<String> cache(final Callable<String> loader) {
        return DocumentServerMetaCache.builder(loader)
                .ttl(Duration.ofSeconds(10))
                .retry(Duration.ofSeconds(1))
                .loadTimeout(Duration.ofSeconds(5))
                .onError(errors::add)
                .clock(clock::get)
                .build();
    }

    private void advance(final Duration duration) {
        clock.addAndGet(duration.toNanos());
    }

    @Test
    public void whenGet_thenLoadOnce() {
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> "v" + calls.incrementAndGet());

        Assertions.assertEquals("v1", cache.get());
        advance(Duration.ofSeconds(9));
        Assertions.assertEquals("v1", cache.get());
        Assertions.assertEquals(1, calls.get());
    }

    @Test
    public void whenStale_thenReload() {
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> "v" + calls.incrementAndGet());

        cache.get();
        advance(Duration.ofSeconds(10));

        Assertions.assertEquals("v2", cache.get());
    }

    @Test
    public void whenFirstLoadFails_thenReturnNullAndReportError() {
        DocumentServerMetaCache<String> cache = cache(() -> {
            throw new IllegalStateException("down");
        });

        Assertions.assertNull(cache.get());
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.get(0) instanceof IllegalStateException);
    }

    @Test
    public void whenLoaderReturnsNull_thenFail() {
        DocumentServerMetaCache<String> cache = cache(() -> null);

        Assertions.assertNull(cache.get());
        Assertions.assertTrue(errors.get(0) instanceof NullPointerException);
    }

    @Test
    public void whenReloadFails_thenKeepOldDataAndWaitForRetry() {
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> {
            if (calls.incrementAndGet() == 2) {
                throw new IllegalStateException("down");
            }

            return "v" + calls.get();
        });

        cache.get();
        advance(Duration.ofSeconds(10));
        Assertions.assertEquals("v1", cache.get());

        advance(Duration.ofMillis(999));
        Assertions.assertEquals("v1", cache.get());
        Assertions.assertEquals(2, calls.get());

        advance(Duration.ofMillis(1));
        Assertions.assertEquals("v3", cache.get());
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    public void whenLoadEndsAfterTimeout_thenIgnoreResult() {
        DocumentServerMetaCache<String> cache = cache(() -> {
            advance(Duration.ofSeconds(5));

            return "late";
        });

        Assertions.assertNull(cache.get());
        Assertions.assertTrue(errors.get(0) instanceof TimeoutException);
    }

    @Test
    public void whenOnErrorThrows_thenGetDoesNotThrow() {
        DocumentServerMetaCache<String> cache = DocumentServerMetaCache.<String>builder(() -> {
                    throw new IllegalStateException("down");
                })
                .onError(e -> {
                    throw new IllegalArgumentException("handler");
                })
                .build();

        Assertions.assertNull(cache.get());
    }

    @Test
    public void whenLoaderThrowsError_thenPropagateAndAllowRetry() {
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> {
            if (calls.incrementAndGet() == 1) {
                throw new AssertionError("fatal");
            }

            return "v";
        });

        Assertions.assertThrows(AssertionError.class, cache::get);
        Assertions.assertNull(cache.get());

        advance(Duration.ofSeconds(1));
        Assertions.assertEquals("v", cache.get());
    }

    @Test
    public void whenLoadRuns_thenOtherCallsDoNotWait() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> {
            if (calls.incrementAndGet() == 1) {
                return "v1";
            }

            started.countDown();
            release.await();

            return "v2";
        });

        cache.get();
        advance(Duration.ofSeconds(10));

        Future<String> loading = executor.submit(cache::get);
        Assertions.assertTrue(started.await(WAIT_SECONDS, TimeUnit.SECONDS));

        Future<String> other = executor.submit(cache::get);
        Assertions.assertEquals("v1", other.get(WAIT_SECONDS, TimeUnit.SECONDS));

        release.countDown();
        Assertions.assertEquals("v2", loading.get(WAIT_SECONDS, TimeUnit.SECONDS));
        Assertions.assertEquals(2, calls.get());
    }

    @Test
    public void whenFirstLoadRuns_thenOtherCallsReturnNull() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        DocumentServerMetaCache<String> cache = cache(() -> {
            started.countDown();
            release.await();

            return "v";
        });

        Future<String> loading = executor.submit(cache::get);
        Assertions.assertTrue(started.await(WAIT_SECONDS, TimeUnit.SECONDS));

        Assertions.assertNull(cache.get());

        release.countDown();
        Assertions.assertEquals("v", loading.get(WAIT_SECONDS, TimeUnit.SECONDS));
    }

    @Test
    public void whenLoadHangsPastTimeout_thenNextCallReportsTimeoutAndLateResultIsIgnored() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> {
            if (calls.incrementAndGet() == 1) {
                started.countDown();
                release.await();

                return "late";
            }

            return "fresh";
        });

        Future<String> hanging = executor.submit(cache::get);
        Assertions.assertTrue(started.await(WAIT_SECONDS, TimeUnit.SECONDS));

        advance(Duration.ofSeconds(5));
        Assertions.assertNull(cache.get());
        Assertions.assertTrue(errors.get(0) instanceof TimeoutException);

        advance(Duration.ofSeconds(1));
        Assertions.assertEquals("fresh", cache.get());

        release.countDown();
        Assertions.assertEquals("fresh", hanging.get(WAIT_SECONDS, TimeUnit.SECONDS));
        Assertions.assertEquals("fresh", cache.get());
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    public void whenInvalidateDuringLoad_thenLoadResultIsIgnored() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> {
            if (calls.incrementAndGet() == 1) {
                started.countDown();
                release.await();

                return "old-server";
            }

            return "new-server";
        });

        Future<String> loading = executor.submit(cache::get);
        Assertions.assertTrue(started.await(WAIT_SECONDS, TimeUnit.SECONDS));

        cache.invalidate();
        release.countDown();
        Assertions.assertNull(loading.get(WAIT_SECONDS, TimeUnit.SECONDS));

        Assertions.assertEquals("new-server", cache.get());
    }

    @Test
    public void whenInvalidateAfterFailure_thenLoadAtOnce() {
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = cache(() -> {
            if (calls.incrementAndGet() == 1) {
                throw new IllegalStateException("down");
            }

            return "v";
        });

        Assertions.assertNull(cache.get());
        cache.invalidate();

        Assertions.assertEquals("v", cache.get());
    }

    @Test
    public void whenDurationsInvalid_thenThrow() {
        DocumentServerMetaCache.Builder<String> builder = DocumentServerMetaCache.builder(() -> "v");

        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.ttl(Duration.ZERO));
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.retry(Duration.ofMillis(-1)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.loadTimeout(Duration.ZERO));
        Assertions.assertThrows(NullPointerException.class, () -> builder.ttl(null));
    }

    @Test
    public void whenHugeDuration_thenDoNotOverflow() {
        AtomicInteger calls = new AtomicInteger();
        DocumentServerMetaCache<String> cache = DocumentServerMetaCache.builder(() -> "v" + calls.incrementAndGet())
                .ttl(Duration.ofSeconds(Long.MAX_VALUE))
                .clock(clock::get)
                .build();

        cache.get();
        advance(Duration.ofDays(365 * 100));

        Assertions.assertEquals("v1", cache.get());
    }
}
