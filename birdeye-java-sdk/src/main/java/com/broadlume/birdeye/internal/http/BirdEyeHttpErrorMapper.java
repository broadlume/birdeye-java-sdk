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
