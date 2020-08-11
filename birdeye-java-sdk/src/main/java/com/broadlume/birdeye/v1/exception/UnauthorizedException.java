package com.broadlume.birdeye.v1.exception;

import com.broadlume.birdeye.v1.model.ErrorResponse;

/**
 * Exception indicating something is wrong with the api key
 */
public class UnauthorizedException extends BirdEyeException {

    public UnauthorizedException(ErrorResponse errorResponse) {
        this(null, null, errorResponse);
    }

    public UnauthorizedException(String message, ErrorResponse errorResponse) {
        this(message, null, errorResponse);
    }

    public UnauthorizedException(Throwable cause, ErrorResponse errorResponse) {
        this(null, cause, errorResponse);
    }

    public UnauthorizedException(String message, Throwable cause, ErrorResponse errorResponse) {
        super(message, cause, 401, errorResponse);
    }
}
