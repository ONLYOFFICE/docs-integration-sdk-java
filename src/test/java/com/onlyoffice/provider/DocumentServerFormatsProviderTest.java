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
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.Format;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentServerFormatsProviderTest {
    private static final long WAIT_SECONDS = 5;

    @Mock
    private DocumentServerClient documentServerClient;

    private final AtomicReference<String> url = new AtomicReference<>("http://ds1");

    private final List<Exception> errors = Collections.synchronizedList(new ArrayList<>());

    private final FormatsProvider fallback = () -> Collections.singletonList(format("bundled"));

    private ExecutorService executor;

    @BeforeEach
    void setup() {
        executor = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    private DocumentServerFormatsProvider provider() {
        return DocumentServerFormatsProvider.builder(documentServerClient, url::get)
                .fallback(fallback)
                .onError(errors::add)
                .build();
    }

    @Test
    public void whenGetFormats_thenReturnFormatsFromClient() {
        when(documentServerClient.getFormats()).thenReturn(Arrays.asList(format("docx"), format("xlsx")));

        Assertions.assertEquals(Arrays.asList("docx", "xlsx"), names(provider().getFormats()));
    }

    @Test
    public void whenGetFormatsTwice_thenClientCalledOnce() {
        when(documentServerClient.getFormats()).thenReturn(Collections.singletonList(format("docx")));
        DocumentServerFormatsProvider provider = provider();

        provider.getFormats();
        provider.getFormats();

        verify(documentServerClient, times(1)).getFormats();
    }

    @Test
    public void whenGetFormats_thenReturnUnmodifiableCopy() {
        List<Format> source = new ArrayList<>(Collections.singletonList(format("docx")));
        when(documentServerClient.getFormats()).thenReturn(source);
        DocumentServerFormatsProvider provider = provider();

        List<Format> formats = provider.getFormats();
        source.add(format("xlsx"));

        Assertions.assertThrows(UnsupportedOperationException.class, () -> formats.add(format("pptx")));
        Assertions.assertEquals(Collections.singletonList("docx"), names(provider.getFormats()));
    }

    @Test
    public void whenUrlChanges_thenReloadFormats() {
        when(documentServerClient.getFormats())
                .thenReturn(Collections.singletonList(format("docx")))
                .thenReturn(Collections.singletonList(format("pdf")));
        DocumentServerFormatsProvider provider = provider();

        Assertions.assertEquals(Collections.singletonList("docx"), names(provider.getFormats()));

        url.set("http://ds2");

        Assertions.assertEquals(Collections.singletonList("pdf"), names(provider.getFormats()));
        Assertions.assertEquals(Collections.singletonList("pdf"), names(provider.getFormats()));
        verify(documentServerClient, times(2)).getFormats();
    }

    @Test
    public void whenUrlChangesDuringLoad_thenResultOfOldServerIsDropped() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        when(documentServerClient.getFormats())
                .thenAnswer(invocation -> {
                    started.countDown();
                    release.await();

                    return Collections.singletonList(format("old"));
                })
                .thenReturn(Collections.singletonList(format("new")));
        DocumentServerFormatsProvider provider = provider();

        Future<List<Format>> loading = executor.submit(provider::getFormats);
        Assertions.assertTrue(started.await(WAIT_SECONDS, TimeUnit.SECONDS));

        url.set("http://ds2");
        Assertions.assertEquals(Collections.singletonList("new"), names(provider.getFormats()));

        release.countDown();
        loading.get(WAIT_SECONDS, TimeUnit.SECONDS);

        Assertions.assertEquals(Collections.singletonList("new"), names(provider.getFormats()));
    }

    @Test
    public void whenFallbackReturnsModifiableList_thenReturnUnmodifiableList() {
        List<Format> source = new ArrayList<>(Collections.singletonList(format("bundled")));
        url.set(null);
        DocumentServerFormatsProvider provider = DocumentServerFormatsProvider.builder(documentServerClient, url::get)
                .fallback(() -> source)
                .build();

        List<Format> formats = provider.getFormats();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> formats.add(format("pptx")));
        Assertions.assertEquals(Collections.singletonList("bundled"), names(source));
    }

    @Test
    public void whenDefaultFallback_thenReturnUnmodifiableList() {
        url.set(null);
        DocumentServerFormatsProvider provider = DocumentServerFormatsProvider.builder(documentServerClient, url::get)
                .build();

        List<Format> formats = provider.getFormats();

        Assertions.assertFalse(formats.isEmpty());
        Assertions.assertThrows(UnsupportedOperationException.class, formats::clear);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> new DefaultFormatsProvider().getFormats()
                .clear());
    }

    @Test
    public void whenUrlEmpty_thenReturnFallbackWithoutRequest() {
        url.set("");

        Assertions.assertEquals(Collections.singletonList("bundled"), names(provider().getFormats()));
        verify(documentServerClient, never()).getFormats();
    }

    @Test
    public void whenUrlNull_thenReturnFallbackWithoutRequest() {
        url.set(null);

        Assertions.assertEquals(Collections.singletonList("bundled"), names(provider().getFormats()));
        verify(documentServerClient, never()).getFormats();
    }

    @Test
    public void whenUrlSupplierThrows_thenReturnFallbackAndReportError() {
        DocumentServerFormatsProvider provider = DocumentServerFormatsProvider.builder(documentServerClient, () -> {
                    throw new IllegalStateException("settings");
                })
                .fallback(fallback)
                .onError(errors::add)
                .build();

        Assertions.assertEquals(Collections.singletonList("bundled"), names(provider.getFormats()));
        Assertions.assertTrue(errors.get(0) instanceof IllegalStateException);
    }

    @Test
    public void whenLoadFails_thenReturnFallbackAndReportError() {
        when(documentServerClient.getFormats()).thenThrow(new IllegalStateException("down"));
        DocumentServerFormatsProvider provider = provider();

        Assertions.assertEquals(Collections.singletonList("bundled"), names(provider.getFormats()));
        Assertions.assertEquals(Collections.singletonList("bundled"), names(provider.getFormats()));
        Assertions.assertEquals(1, errors.size());
        verify(documentServerClient, times(1)).getFormats();
    }

    @Test
    public void whenClientReturnsNull_thenReturnFallback() {
        when(documentServerClient.getFormats()).thenReturn(null);

        Assertions.assertEquals(Collections.singletonList("bundled"), names(provider().getFormats()));
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    public void whenFallbackThrowsAndUrlEmpty_thenReturnEmptyListAndReportError() {
        url.set("");

        Assertions.assertTrue(failingFallbackProvider().getFormats().isEmpty());
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("fallback", errors.get(0).getMessage());
    }

    @Test
    public void whenFallbackThrowsAndLoadFails_thenReturnEmptyListAndReportBothErrors() {
        when(documentServerClient.getFormats()).thenThrow(new IllegalStateException("down"));

        Assertions.assertTrue(failingFallbackProvider().getFormats().isEmpty());
        Assertions.assertEquals(2, errors.size());
        Assertions.assertEquals("down", errors.get(0).getMessage());
        Assertions.assertEquals("fallback", errors.get(1).getMessage());
    }

    @Test
    public void whenFallbackThrowsAndUrlSupplierThrows_thenReturnEmptyList() {
        DocumentServerFormatsProvider provider = DocumentServerFormatsProvider.builder(documentServerClient, () -> {
                    throw new IllegalStateException("settings");
                })
                .fallback(() -> {
                    throw new IllegalStateException("fallback");
                })
                .onError(errors::add)
                .build();

        Assertions.assertTrue(provider.getFormats().isEmpty());
        Assertions.assertEquals(2, errors.size());
    }

    @Test
    public void whenFallbackReturnsNull_thenReturnEmptyList() {
        url.set("");
        DocumentServerFormatsProvider provider = DocumentServerFormatsProvider.builder(documentServerClient, url::get)
                .fallback(() -> null)
                .build();

        Assertions.assertTrue(provider.getFormats().isEmpty());
    }

    private DocumentServerFormatsProvider failingFallbackProvider() {
        return DocumentServerFormatsProvider.builder(documentServerClient, url::get)
                .fallback(() -> {
                    throw new IllegalStateException("fallback");
                })
                .onError(errors::add)
                .build();
    }

    @Test
    public void whenLoadFailsAndUrlChanges_thenRetryAtOnce() {
        when(documentServerClient.getFormats())
                .thenThrow(new IllegalStateException("down"))
                .thenReturn(Collections.singletonList(format("docx")));
        DocumentServerFormatsProvider provider = provider();

        provider.getFormats();
        url.set("http://ds2");

        Assertions.assertEquals(Collections.singletonList("docx"), names(provider.getFormats()));
    }

    @Test
    public void whenFirstLoadRuns_thenOtherCallsReturnFallbackWithoutWaiting() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        when(documentServerClient.getFormats()).thenAnswer(invocation -> {
            started.countDown();
            release.await();

            return Collections.singletonList(format("docx"));
        });
        DocumentServerFormatsProvider provider = provider();

        Future<List<Format>> loading = executor.submit(provider::getFormats);
        Assertions.assertTrue(started.await(WAIT_SECONDS, TimeUnit.SECONDS));

        Assertions.assertEquals(Collections.singletonList("bundled"), names(provider.getFormats()));

        release.countDown();
        Assertions.assertEquals(Collections.singletonList("docx"), names(loading.get(WAIT_SECONDS, TimeUnit.SECONDS)));
        verify(documentServerClient, times(1)).getFormats();
    }

    @Test
    public void whenInvalidateCache_thenReloadFormats() {
        when(documentServerClient.getFormats())
                .thenReturn(Collections.singletonList(format("docx")))
                .thenReturn(Collections.singletonList(format("pdf")));
        DocumentServerFormatsProvider provider = provider();

        provider.getFormats();
        provider.invalidateCache();

        Assertions.assertEquals(Collections.singletonList("pdf"), names(provider.getFormats()));
    }

    @Test
    public void whenConstructedWithUrlManager_thenUseInnerDocumentServerUrl() {
        UrlManager urlManager = org.mockito.Mockito.mock(UrlManager.class);
        when(urlManager.getInnerDocumentServerUrl()).thenReturn("http://ds1");
        when(documentServerClient.getFormats()).thenReturn(Collections.singletonList(format("docx")));

        DocumentServerFormatsProvider provider = new DocumentServerFormatsProvider(documentServerClient, urlManager);

        Assertions.assertEquals(Collections.singletonList("docx"), names(provider.getFormats()));
        verify(urlManager).getInnerDocumentServerUrl();
    }

    private static Format format(final String name) {
        Format format = new Format();
        format.setName(name);
        format.setActions(Collections.singletonList("view"));
        format.setConvert(Collections.emptyList());
        format.setMime(Collections.emptyList());

        return format;
    }

    private static List<String> names(final List<Format> formatList) {
        List<String> result = new ArrayList<>();

        for (Format format : formatList) {
            result.add(format.getName());
        }

        return result;
    }
}
