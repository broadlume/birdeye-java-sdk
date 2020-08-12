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
