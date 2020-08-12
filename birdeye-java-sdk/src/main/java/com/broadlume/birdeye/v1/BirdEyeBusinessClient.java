package com.broadlume.birdeye.v1;

import com.broadlume.birdeye.internal.http.HttpClient;
import com.broadlume.birdeye.v1.model.Business;
import com.fasterxml.jackson.core.JsonProcessingException;
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
