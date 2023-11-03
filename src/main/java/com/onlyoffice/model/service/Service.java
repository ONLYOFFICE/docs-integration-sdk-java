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

package com.onlyoffice.model.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines the server service.
 */
@Getter
@AllArgsConstructor
public enum Service {
    /**
     * Defines a path to the Document Server.
     */
    DOCUMENT_SERVER("/"),

    /**
     * Defines a path to the Converting Service.
     */
    CONVERT_SERVICE("/ConvertService.ashx"),

    /**
     * Defines a path to the Command Service.
     */
    COMMAND_SERVICE("/coauthoring/CommandService.ashx");

    /**
     * Defines a path to the server service.
     */
    private final String path;
}
