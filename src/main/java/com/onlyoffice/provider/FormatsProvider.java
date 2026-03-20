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

import com.onlyoffice.model.common.Format;

import java.util.List;


/**
 * Defines the contract for supplying document format definitions.
 * Implementations are responsible for loading or providing the list of formats
 * supported by ONLYOFFICE editors.
 */
public interface FormatsProvider {

    /**
     * Returns a list of document formats supported by the ONLYOFFICE editors.
     *
     * @return A list of supported formats.
     */
    List<Format> getFormats();
}
