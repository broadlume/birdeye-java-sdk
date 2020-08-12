package com.broadlume.birdeye.v1.exception;

import com.broadlume.birdeye.v1.model.ErrorResponse;

/**
 * Exception indicating the rate limit has been exceeded
 */
public class TooManyRequestsException extends BirdEyeException {

    public TooManyRequestsException(ErrorResponse errorResponse) {
        this(null, null, errorResponse);
    }

    public TooManyRequestsException(String message, ErrorResponse errorResponse) {
        this(message, null, errorResponse);
    }

    public TooManyRequestsException(Throwable cause, ErrorResponse errorResponse) {
        this(null, cause, errorResponse);
    }

    public TooManyRequestsException(String message, Throwable cause, ErrorResponse errorResponse) {
        super(message, cause, 429, errorResponse);
    }
}
