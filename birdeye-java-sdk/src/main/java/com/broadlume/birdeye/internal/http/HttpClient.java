package com.broadlume.birdeye.internal.http;

import com.broadlume.birdeye.v1.exception.BirdEyeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.Objects;

/**
 * Client for making HTTP requests<br>
 * Wraps AsyncHttpClient to provide error handling and response parsing
 */
public class HttpClient {

    private final AsyncHttpClient asyncHttpClient;
    private final ObjectMapper objectMapper;
    private final HttpErrorMapper<BirdEyeException> errorMapper;

    public HttpClient(AsyncHttpClient asyncHttpClient, ObjectMapper objectMapper,
                      HttpErrorMapper<BirdEyeException> errorMapper) {
        this.asyncHttpClient = Objects.requireNonNull(asyncHttpClient);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.errorMapper = Objects.requireNonNull(errorMapper);
    }

    /**
     * Execute a request asynchronously
     * @param request the request
     * @return the response
     */
    public Single<Response> execute(Request request) {
        Objects.requireNonNull(request);
        return Single.defer(() -> AsyncHttpReactiveUtils.toSingle(() -> asyncHttpClient.executeRequest(request)))
                .flatMap(response -> checkResponseForError(response));
    }

    /**
     * Execute a request asynchronously and parse the response as JSON
     * @param request the request
     * @param responseClazz the expected type of the response
     * @param <T> the response type
     * @return the parsed response
     */
    public <T> Single<T> execute(Request request, Class<T> responseClazz) {
        return execute(request)
                .map(response -> objectMapper.readValue(response.getResponseBody(), responseClazz));
    }

    /**
     * Check and handle an error in the response
     * @param response the response
     * @return the response or an error if found
     */
    private Single<Response> checkResponseForError(Response response) {
        return errorMapper.hasError(response) ?
                Single.error(errorMapper.mapResponseError(response)) :
                Single.just(response);
    }
}
