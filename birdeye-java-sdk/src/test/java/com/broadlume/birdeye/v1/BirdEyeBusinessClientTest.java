package com.broadlume.birdeye.v1;

import com.broadlume.birdeye.internal.http.HttpClient;
import com.broadlume.birdeye.v1.model.Address;
import com.broadlume.birdeye.v1.model.Business;
import com.broadlume.birdeye.v1.model.SocialProfileUrls;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BirdEyeBusinessClientTest {

    private HttpClient httpClient = mock(HttpClient.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private BirdEyeBusinessClient client = new BirdEyeBusinessClient(httpClient, objectMapper, "https://test.url",
            "abcd1234");

    @Test
    public void getTest() throws InterruptedException {
        Business b = Business.builder().name("test").alias("test").phone("1234").websiteUrl("www").description("descr")
                .keywords("1,2").services("2,1").logoUrl("www").coverImageUrl("www").image1Url("").image2Url("")
                .image3Url("").language("english").payment("card").isSEOEnabled("false").working24x7(0).widLabel("")
                .widBGColor("").location(Address.builder().address1("1234 street").city("city").state("state")
                        .countryCode("US").countryName("US").zip("1234").build())
                .socialProfileURLs(SocialProfileUrls.builder().build()).build();
        TestObserver<Void> probe = TestObserver.create();
        when(httpClient.execute(argThat(arg ->
                        arg != null &&
                                arg.getUrl().equals("https://test.url/business/1234?api_key=abcd1234") &&
                                arg.getMethod().equals("GET") &&
                                arg.getHeaders().containsValue("Accept", "application/json", true)),
                eq(Business.class))).thenReturn(Single.just(b).doOnSubscribe(d -> probe.onSubscribe(d)));

        Single.fromPublisher(client.get(1234))
                .test().await().assertResult(b);
        probe.assertEmpty();
    }
}
