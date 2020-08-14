package com.broadlume.birdeye;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class BirdEyeV1ClientBuilderTest {

    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private AsyncHttpClient asyncHttpClient = mock(AsyncHttpClient.class);

    @Test
    public void buildBusinessClientTest() {
        BirdEyeConfig config = BirdEyeConfig.builder()
                .objectMapper(objectMapper)
                .asyncHttpClient(asyncHttpClient)
                .apiKey("test-key")
                .baseUrl("https://test.url")
                .build();
        assertNotNull(new BirdEyeV1ClientBuilder(config).buildBusinessClient());
    }
}
