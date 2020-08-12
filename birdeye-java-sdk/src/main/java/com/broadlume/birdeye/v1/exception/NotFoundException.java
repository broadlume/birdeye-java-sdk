package com.broadlume.birdeye.v1.exception;

import com.broadlume.birdeye.v1.model.ErrorResponse;

/**
 * Exception indicating the requested data was not found
 */
public class NotFoundException extends BirdEyeException {

    public NotFoundException(ErrorResponse errorResponse) {
        this(null, null, errorResponse);
    }

    public NotFoundException(String message, ErrorResponse errorResponse) {
        this(message, null, errorResponse);
    }

    public NotFoundException(Throwable cause, ErrorResponse errorResponse) {
        this(null, cause, errorResponse);
    }

    public NotFoundException(String message, Throwable cause, ErrorResponse errorResponse) {
        super(message, cause, 404, errorResponse);
    }
}
