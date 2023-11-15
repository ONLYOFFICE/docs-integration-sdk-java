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

package com.onlyoffice.model.common;


import com.onlyoffice.model.documenteditor.config.document.DocumentType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Defines the document format properties.
 */
@Getter
@Setter
public class Format {
    /**
     * Defines the document format name.
     */
    private String name;

    /**
     * Defines the document format type.
     */
    private DocumentType type;

    /**
     * Defines a list of actions that can be performed with the current document format.
     */
    private List<String>  actions;

    /**
     * Defines a list of extensions to which the current format can be converted.
     */
    private List<String> convert;

    /**
     * Defines a list of MIME types of the current document format.
     */
    private List<String> mime;
}
