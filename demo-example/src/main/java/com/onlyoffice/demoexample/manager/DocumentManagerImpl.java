/**
 *
 * (c) Copyright Ascensio System SIA 2025
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

package com.onlyoffice.demoexample.manager;

import com.onlyoffice.manager.document.DefaultDocumentManager;
import com.onlyoffice.manager.settings.SettingsManager;
import org.springframework.stereotype.Component;

public class DocumentManagerImpl extends DefaultDocumentManager {
    public DocumentManagerImpl(final SettingsManager settingsManager) {
        super(settingsManager);
    }

    @Override
    public String getDocumentKey(final  String fileId, final boolean embedded) {
        return String.valueOf(fileId.hashCode());
    }

    @Override
    public String getDocumentName(final String fileId) {
        return "sample.docx";
    }
}
