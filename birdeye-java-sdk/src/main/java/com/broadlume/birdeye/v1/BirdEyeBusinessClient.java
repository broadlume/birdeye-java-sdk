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

package com.broadlume.birdeye.v1;

import com.broadlume.birdeye.internal.http.HttpClient;
import com.broadlume.birdeye.v1.model.Business;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.reactivestreams.Publisher;

import java.util.Objects;

public class BirdEyeBusinessClient {

    private final HttpClient http;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String apiKey;

    public BirdEyeBusinessClient(HttpClient http, ObjectMapper objectMapper, String baseUrl, String apiKey) {
        this.http = Objects.requireNonNull(http);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.baseUrl = Objects.requireNonNull(baseUrl);
        this.apiKey = Objects.requireNonNull(apiKey);
    }

    // TODO auto-retry Too Many Requests exceptions

    /**
     * Get a business by its ID
     * @param id the business ID
     * @return a Publisher that emits the single matching business
     */
    public Publisher<Business> get(long id) {
        Request request = new RequestBuilder("GET")
                .setUrl(baseUrl + "/business/" + id)
                .addHeader("Accept", "application/json")
                .addQueryParam("api_key", apiKey)
                .build();
        return http.execute(request, Business.class)
                .toFlowable();
    }
}
