package com.broadlume.birdeye.v1.exception;

import com.broadlume.birdeye.v1.model.ErrorResponse;

/**
 * Exception indicating something is wrong with the request
 */
public class BadRequestException extends BirdEyeException {

    public BadRequestException(ErrorResponse errorResponse) {
        this(null, null, errorResponse);
    }

    public BadRequestException(String message, ErrorResponse errorResponse) {
        this(message, null, errorResponse);
    }

    public BadRequestException(Throwable cause, ErrorResponse errorResponse) {
        this(null, cause, errorResponse);
    }

    public BadRequestException(String message, Throwable cause, ErrorResponse errorResponse) {
        super(message, cause, 400, errorResponse);
    }
}
