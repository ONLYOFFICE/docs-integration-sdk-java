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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.model.common.Format;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class DefaultFormatsProvider implements FormatsProvider {
    /**
     * Path to the matrix of formats supported by the ONLYOFFICE Editor.
     */
    private static final String DOCS_FORMATS_JSON_PATH =
            "assets/document-formats/onlyoffice-docs-formats.json";

    /** Defines a list containing data about supported formats. */
    private static List<Format> formats;

    static {
        init();
    }

    @Override
    public List<Format> getFormats() {
        return formats;
    }

    protected static void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(DOCS_FORMATS_JSON_PATH);

        if (inputStream == null) {
            inputStream = DefaultFormatsProvider.class
                    .getClassLoader()
                    .getResourceAsStream(DOCS_FORMATS_JSON_PATH);
        }

        try {
            formats = objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                    .readValue(inputStream, new TypeReference<List<Format>>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
