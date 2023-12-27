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

package com.onlyoffice.model.properties.docsintegrationsdk.documentserver;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class EditingServiceProperties {
    private Long maxFileSize;
    private String mobileUserAgent;
    private String insertImage;

    public List<String> getInsertImage() {
        if (insertImage != null && !insertImage.isEmpty()) {
            return Arrays.asList(insertImage.split("\\|"));
        }

        return null;
    }
}
