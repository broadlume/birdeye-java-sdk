package com.broadlume.birdeye;

import com.broadlume.birdeye.internal.http.BirdEyeHttpErrorMapper;
import com.broadlume.birdeye.internal.http.HttpClient;
import com.broadlume.birdeye.v1.BirdEyeBusinessClient;

import java.util.Objects;

/**
 * Creates instances of BirdEye resource clients
 */
public class BirdEyeV1ClientBuilder {

    private final BirdEyeConfig config;
    private final HttpClient httpClient;

    public BirdEyeV1ClientBuilder(BirdEyeConfig config) {
        this.config = Objects.requireNonNull(config);
        httpClient = new HttpClient(config.getAsyncHttpClient(), config.getObjectMapper(),
                new BirdEyeHttpErrorMapper(config.getObjectMapper()));
    }

    public BirdEyeBusinessClient buildBusinessClient() {
        return new BirdEyeBusinessClient(httpClient, config.getObjectMapper(), config.getBaseUrl(), config.getApiKey());
    }
}
