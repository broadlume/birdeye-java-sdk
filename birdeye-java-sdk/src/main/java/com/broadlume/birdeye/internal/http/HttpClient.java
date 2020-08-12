/*
 * Copyright 2020, birdeye-java-sdk Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.broadlume.birdeye.internal.http;

import com.broadlume.birdeye.v1.exception.BirdEyeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.Objects;

/**
 * Client for making HTTP requests<br>
 * Wraps AsyncHttpClient to provide error handling and response parsing
 */
public class HttpClient {

    private final AsyncHttpClient asyncHttpClient;
    private final ObjectMapper objectMapper;
    private final HttpErrorMapper<BirdEyeException> errorMapper;

    public HttpClient(AsyncHttpClient asyncHttpClient, ObjectMapper objectMapper,
                      HttpErrorMapper<BirdEyeException> errorMapper) {
        this.asyncHttpClient = Objects.requireNonNull(asyncHttpClient);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.errorMapper = Objects.requireNonNull(errorMapper);
    }

    /**
     * Execute a request asynchronously
     * @param request the request
     * @return the response
     */
    public Single<Response> execute(Request request) {
        Objects.requireNonNull(request);
        return Single.defer(() -> AsyncHttpReactiveUtils.toSingle(() -> asyncHttpClient.executeRequest(request)))
                .flatMap(response -> checkResponseForError(response));
    }

    /**
     * Execute a request asynchronously and parse the response as JSON
     * @param request the request
     * @param responseClazz the expected type of the response
     * @param <T> the response type
     * @return the parsed response
     */
    public <T> Single<T> execute(Request request, Class<T> responseClazz) {
        return execute(request)
                .map(response -> objectMapper.readValue(response.getResponseBody(), responseClazz));
    }

    /**
     * Check and handle an error in the response
     * @param response the response
     * @return the response or an error if found
     */
    private Single<Response> checkResponseForError(Response response) {
        return errorMapper.hasError(response) ?
                Single.error(errorMapper.mapResponseError(response)) :
                Single.just(response);
    }
}
