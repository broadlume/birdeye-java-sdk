package com.broadlume.birdeye;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

import javax.annotation.Nonnull;

@Value @Builder @AllArgsConstructor
public class BirdEyeConfig {

    @Nonnull @Builder.Default
    ObjectMapper objectMapper = new ObjectMapper();
    @Nonnull @Builder.Default
    AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
    @Nonnull
    String apiKey;
    @Nonnull @Builder.Default
    String baseUrl = "https://api.birdeye.com/resources";
}
