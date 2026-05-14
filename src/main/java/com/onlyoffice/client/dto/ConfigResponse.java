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

package com.onlyoffice.client.dto;

import lombok.Data;

import java.util.List;


@Data
public class ConfigResponse {
    private Authorization authorization;
    private Urls urls;
    private Limits limits;
    private List<String> langs;

    @Data
    public static class Authorization {
        private String header;
        private String prefix;
    }

    @Data
    public static class Urls {
        private String api;
        private String command;
        private String converter;
        private String docbuilder;
    }

    @Data
    public static class Limits {
        private Long maxFileSize;
    }
}
