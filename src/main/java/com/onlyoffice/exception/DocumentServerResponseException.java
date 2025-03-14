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

package com.onlyoffice.exception;

public class DocumentServerResponseException extends RuntimeException {
    /**
     * Stores the request that caused the exception.
     */
    private final String request;

    /**
     * Constructs a new DocumentServerResponseException with the specified cause and request.
     *
     * @param e The cause of the exception (a {@link Throwable})
     * @param request The request that caused the exception
     */
    public DocumentServerResponseException(final Throwable e, final String request) {
        super(e);

        this.request = request;
    }

    @Override
    public String toString() {
        return super.toString() + "\nRequest: " + request + "\n";
    }
}
