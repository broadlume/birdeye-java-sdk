package com.broadlume.birdeye.internal.http;

import org.asynchttpclient.Response;

/**
 * Maps HTTP responses to exceptions
 * @param <T> the type of exceptions returned
 */
public interface HttpErrorMapper<T extends Exception> {

    /**
     * Check if a response is an error response
     * @param response the response
     * @return if it is an error
     */
    boolean hasError(Response response);

    /**
     * Create an exception from an error response
     * @param response the error response
     * @return the exception
     */
    T mapResponseError(Response response);
}
