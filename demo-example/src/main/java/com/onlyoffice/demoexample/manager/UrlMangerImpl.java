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

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UrlMangerImpl extends DefaultUrlManager {
    @Autowired
    private HttpServletRequest request;

    public UrlMangerImpl(final SettingsManager settingsManager) {
        super(settingsManager);
    }

    @Override
    public String getFileUrl(final String fileId) {
        return getServerUrl() + "/file/download";
    }

    @Override
    public String getCallbackUrl(final String fileId) {
        return getServerUrl() + "/callback";
    }

    private String getServerUrl() {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
    }

}
