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

package com.onlyoffice.service.convert;

import com.onlyoffice.model.convert.Convert;
import org.json.JSONObject;

public interface ConvertService {

    /**
     * Starts the file conversion process.
     *
     * @param convert The {@link Convert} object with the request parameters that are sent to the "https://documentserver/ConvertService.ashx" address.
     * @param fileId The ID of the file to be converted.
     * @return The JSON object containing the body of the response from the conversion service.
     * @throws Exception
     */
    JSONObject processConvert(Convert convert, String fileId) throws Exception;
}
