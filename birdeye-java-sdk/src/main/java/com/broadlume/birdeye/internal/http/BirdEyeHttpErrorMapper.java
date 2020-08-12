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
import com.broadlume.birdeye.v1.model.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Maps error responses to {@link BirdEyeException}s
 */
public class BirdEyeHttpErrorMapper implements HttpErrorMapper<BirdEyeException> {

    private static final Logger logger = LoggerFactory.getLogger(BirdEyeHttpErrorMapper.class);

    private final ObjectMapper objectMapper;

    public BirdEyeHttpErrorMapper(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    /**
     * Check if a response is an error response
     * @param response the response
     * @return if it is an error
     */
    @Override
    public boolean hasError(Response response) {
        int family = response.getStatusCode() / 100;
        return family != 2;
    }

    /**
     * Create an exception from an error response
     * @param response the error response
     * @return the exception
     */
    @Override
    public BirdEyeException mapResponseError(Response response) {
        ErrorResponse errorResponse = getExceptionMessage(response);
        BirdEyeException e;

        switch (response.getStatusCode()) {
            case 400:
                e = new BadRequestException(errorResponse);
                break;
            case 401:
                e = new UnauthorizedException(errorResponse);
                break;
            case 404:
                e = new NotFoundException(errorResponse);
                break;
            case 429:
                e = new TooManyRequestsException(errorResponse);
                break;
            default:
                e = new BirdEyeException(response.getStatusCode(), errorResponse);
        }

        return e;
    }

    /**
     * Extract the exception message from an error response
     * @param response the error response
     * @return the exception message
     */
    private ErrorResponse getExceptionMessage(Response response) {
        String responseBody = response.getResponseBody();
        ErrorResponse errorResponse = null;

        try {
            errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            logger.debug("Failed to parse response body to ErrorMessage", e);
        }
        if (errorResponse == null || (errorResponse.getErrorCode() == 0 && errorResponse.getErrorMessage() == null))
            errorResponse = ErrorResponse.builder().errorCode(0).errorMessage(responseBody).build();

        return errorResponse;
    }
}
