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
import com.broadlume.birdeye.v1.model.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class HttpClientTest {

    private AsyncHttpClient asyncHttpClient = mock(AsyncHttpClient.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private HttpErrorMapper<BirdEyeException> errorMapper = mock(HttpErrorMapper.class);
    private HttpClient httpClient = new HttpClient(asyncHttpClient, objectMapper, errorMapper);
    private CompletableFuture<Response> cf = new CompletableFuture<>();
    private ListenableFuture<Response> future = mock(ListenableFuture.class);
    private Response response = mock(Response.class);

    @Before
    public void setup() {
        when(future.toCompletableFuture()).thenReturn(cf);
        cf.complete(response);
    }

    /**
     * Should pass the request through to AsyncHttpClient, convert the ListenableFuture to reactive, and return the response
     */
    @Test
    public void executeTest() throws InterruptedException {
        Request request = new RequestBuilder().build();
        when(asyncHttpClient.executeRequest(request)).thenReturn(future);

        httpClient.execute(request)
                .test().await()
                .assertResult(response);
    }

    /**
     * Should use the ErrorMapper to map the response to an error if it indicates the response failed
     */
    @Test
    public void executeWithErrorTest() throws InterruptedException {
        Request request = new RequestBuilder().build();
        when(asyncHttpClient.executeRequest(request)).thenReturn(future);
        when(errorMapper.hasError(response)).thenReturn(true);
        when(errorMapper.mapResponseError(response)).thenReturn(new BirdEyeException(500, new ErrorResponse(0, "test")));

        httpClient.execute(request)
                .test().await()
                .assertError(BirdEyeException.class);
    }

    /**
     * Should parse the response into an object
     */
    @Test
    public void executeAndParseResponseTest() throws InterruptedException, JsonProcessingException {
        Request request = new RequestBuilder().build();
        when(asyncHttpClient.executeRequest(request)).thenReturn(future);
        when(response.getResponseBody()).thenReturn("test-response");
        when(objectMapper.readValue("test-response", ErrorResponse.class))
                .thenReturn(new ErrorResponse(200, "success!"));

        httpClient.execute(request, ErrorResponse.class)
                .test().await()
                .assertResult(new ErrorResponse(200, "success!"));
    }
}
