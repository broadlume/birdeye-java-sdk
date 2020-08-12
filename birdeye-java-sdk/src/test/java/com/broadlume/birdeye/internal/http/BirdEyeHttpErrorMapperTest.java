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

import com.broadlume.birdeye.v1.exception.BadRequestException;
import com.broadlume.birdeye.v1.exception.BirdEyeException;
import com.broadlume.birdeye.v1.exception.NotFoundException;
import com.broadlume.birdeye.v1.exception.TooManyRequestsException;
import com.broadlume.birdeye.v1.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BirdEyeHttpErrorMapperTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private BirdEyeHttpErrorMapper mapper = new BirdEyeHttpErrorMapper(objectMapper);
    private Response response = mock(Response.class);

    @Test
    public void hasErrorTest() {
        when(response.getStatusCode()).thenReturn(200);
        assertFalse(mapper.hasError(response));

        when(response.getStatusCode()).thenReturn(500);
        assertTrue(mapper.hasError(response));
    }

    @Test
    public void mapResponseErrorBadRequestTest() {
        when(response.getStatusCode()).thenReturn(400);
        when(response.getResponseBody()).thenReturn("{\"errorCode\":100,\"errorMessage\":\"test message\"}");

        BirdEyeException e = mapper.mapResponseError(response);
        assertEquals(BadRequestException.class, e.getClass());
        assertEquals(100, e.getErrorResponse().getErrorCode());
        assertEquals("test message", e.getErrorResponse().getErrorMessage());
    }

    @Test
    public void mapResponseErrorUnauthorizedTest() {
        when(response.getStatusCode()).thenReturn(401);
        when(response.getResponseBody()).thenReturn("{\"errorCode\":100,\"errorMessage\":\"test message\"}");

        BirdEyeException e = mapper.mapResponseError(response);
        assertEquals(UnauthorizedException.class, e.getClass());
        assertEquals(100, e.getErrorResponse().getErrorCode());
        assertEquals("test message", e.getErrorResponse().getErrorMessage());
    }

    @Test
    public void mapResponseErrorNotFoundTest() {
        when(response.getStatusCode()).thenReturn(404);
        when(response.getResponseBody()).thenReturn("{\"errorCode\":100,\"errorMessage\":\"test message\"}");

        BirdEyeException e = mapper.mapResponseError(response);
        assertEquals(NotFoundException.class, e.getClass());
        assertEquals(100, e.getErrorResponse().getErrorCode());
        assertEquals("test message", e.getErrorResponse().getErrorMessage());
    }

    @Test
    public void mapResponseErrorTooManyRequestsTest() {
        when(response.getStatusCode()).thenReturn(429);
        when(response.getResponseBody()).thenReturn("{\"errorCode\":100,\"errorMessage\":\"test message\"}");

        BirdEyeException e = mapper.mapResponseError(response);
        assertEquals(TooManyRequestsException.class, e.getClass());
        assertEquals(100, e.getErrorResponse().getErrorCode());
        assertEquals("test message", e.getErrorResponse().getErrorMessage());
    }

    @Test
    public void mapResponseErrorUnknownErrorTest() {
        when(response.getStatusCode()).thenReturn(500);
        when(response.getResponseBody()).thenReturn("{\"errorCode\":100,\"errorMessage\":\"test message\"}");

        BirdEyeException e = mapper.mapResponseError(response);
        assertEquals(BirdEyeException.class, e.getClass());
        assertEquals(100, e.getErrorResponse().getErrorCode());
        assertEquals("test message", e.getErrorResponse().getErrorMessage());
    }

    /**
     * Should make up a default error response if birdeye did not return one
     */
    @Test
    public void mapResponseErrorEmptyBodyTest() {
        when(response.getStatusCode()).thenReturn(500);
        when(response.getResponseBody()).thenReturn("");

        BirdEyeException e = mapper.mapResponseError(response);
        assertEquals(BirdEyeException.class, e.getClass());
        assertEquals(0, e.getErrorResponse().getErrorCode());
        assertEquals("", e.getErrorResponse().getErrorMessage());
    }

    /**
     * Should make up a default error response if unable to parse the response body
     */
    @Test
    public void mapResponseErrorBadBodyTest() {
        when(response.getStatusCode()).thenReturn(500);
        when(response.getResponseBody()).thenReturn("{x}");

        BirdEyeException e = mapper.mapResponseError(response);
        assertEquals(BirdEyeException.class, e.getClass());
        assertEquals(0, e.getErrorResponse().getErrorCode());
        assertEquals("{x}", e.getErrorResponse().getErrorMessage());
    }
}
