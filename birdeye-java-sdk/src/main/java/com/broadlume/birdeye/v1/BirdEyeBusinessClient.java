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
import com.broadlume.birdeye.v1.model.ChildBusiness;
import com.broadlume.birdeye.v1.model.CreateBusinessRequest;
import com.broadlume.birdeye.v1.model.CreateBusinessResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
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

    /**
     * Update an existing business
     * @param id the business ID
     * @param business the business details to set
     * @return a Publisher that emits a completion signal when done
     */
    public Publisher<Void> update(long id, Business business) {
        // birdeye returns dates like 'Aug 13, 2020' but only accepts dates formatted as yyyy-MM-dd HH:mm:ss
        // if the created date is set, clear it so that we don't have to fix it, which will leave it at the original value
        if (business.getCreatedDate() != null)
            business = business.toBuilder().createdDate(null).build();

        Request request = new RequestBuilder("PUT")
                .setUrl(baseUrl + "/v1/business/" + id)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addQueryParam("api_key", apiKey)
                .setBody(writeAsJson(business))
                .build();
        return http.execute(request)
                .ignoreElement().toFlowable();
    }

    /**
     * Delete a business by ID
     * @param id the business ID
     * @return a Publisher that emits a completion signal when done
     */
    public Publisher<Void> delete(long id) {
        Request request = new RequestBuilder("DELETE")
                .setUrl(baseUrl + "/v1/business/" + id)
                .addHeader("Accept", "application/json")
                .addQueryParam("api_key", apiKey)
                .build();
        return http.execute(request)
                .ignoreElement().toFlowable();
    }

    /**
     * Retrieve all child businesses under a parent business
     * @param parentId the parent business ID
     * @return a Publisher that emits each child business
     */
    public Publisher<ChildBusiness> getChildren(long parentId) {
        Request request = new RequestBuilder("GET")
                .setUrl(baseUrl + "/v1/business/child/all")
                .addHeader("Accept", "application/json")
                .addQueryParam("pid", String.valueOf(parentId))
                .addQueryParam("api_key", apiKey)
                .build();
        return http.execute(request, ChildBusiness[].class)
                .flatMapPublisher(businesses -> Flowable.fromArray(businesses));
    }

    private String writeAsJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException("Unable to write body as JSON", e);
        }
    }
}
