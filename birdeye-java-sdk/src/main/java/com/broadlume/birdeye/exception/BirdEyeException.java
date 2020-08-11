package com.broadlume.birdeye.exception;

import com.broadlume.birdeye.v1.model.ErrorResponse;

/**
 * Base class for all exceptions from BirdEye
 */
public class BirdEyeException extends RuntimeException {

    private final int statusCode;
    private final ErrorResponse errorResponse;

    public BirdEyeException(int statusCode, ErrorResponse errorResponse) {
        this(null, null, statusCode, errorResponse);
    }

    public BirdEyeException(String message, int statusCode, ErrorResponse errorResponse) {
        this(message, null, statusCode, errorResponse);
    }

    public BirdEyeException(Throwable cause, int statusCode, ErrorResponse errorResponse) {
        this(null, cause, statusCode, errorResponse);
    }

    public BirdEyeException(String message, Throwable cause, int statusCode, ErrorResponse errorResponse) {
        super(buildMessage(message, errorResponse), cause);
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    private static String buildMessage(String message, ErrorResponse errorResponse) {
        String result;
        if (message != null)
            result = message;
        else if (errorResponse != null)
            result = errorResponse.getErrorMessage();
        else
            result = null;
        return result;
    }
}
