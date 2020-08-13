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
import com.broadlume.birdeye.v1.model.CreateBusinessRequest;
import com.broadlume.birdeye.v1.model.CreateBusinessResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.reactivestreams.Publisher;

import java.io.UncheckedIOException;
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

    /**
     * Get a business by its ID
     * @param id the business ID
     * @return a Publisher that emits the single matching business
     */
    public Publisher<Business> get(long id) {
        Request request = new RequestBuilder("GET")
                .setUrl(baseUrl + "/v1/business/" + id)
                .addHeader("Accept", "application/json")
                .addQueryParam("api_key", apiKey)
                .build();
        return http.execute(request, Business.class)
                .toFlowable();
    }

    /**
     * Create a new business
     * @param resellerId the reseller ID to create the business under
     * @param emailId an email address, does not get associated with the business so purpose is unknown
     * @param createRequest data for the new business
     * @return a Publisher that emits a single response containing the ID of the created business
     */
    public Publisher<CreateBusinessResponse> create(long resellerId, String emailId, CreateBusinessRequest createRequest) {
        Request request = new RequestBuilder("POST")
                .setUrl(baseUrl + "/v1/signup/reseller/subaccount")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addQueryParam("rid", String.valueOf(resellerId))
                .addQueryParam("email_id", Objects.requireNonNull(emailId))
                .addQueryParam("api_key", apiKey)
                .setBody(writeAsJson(createRequest))
                .build();
        return http.execute(request, CreateBusinessResponse.class)
                .toFlowable();
    }

    private String writeAsJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException("Unable to write body as JSON", e);
        }
    }
}
