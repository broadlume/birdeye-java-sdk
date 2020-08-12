package com.broadlume.birdeye.internal.http;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.asynchttpclient.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * Utilities for converting between reactive and futures used by AsyncHttpClient
 */
public class AsyncHttpReactiveUtils {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpReactiveUtils.class);

    /**
     * Convert an AsyncHttpClient request to a Single
     * @param supplier the supplier that executes the AsyncHttpClient request and returns a ListenableFuture
     * @param <T> the type returned by the future
     * @return the single
     */
    public static <T> Single<T> toSingle(Supplier<ListenableFuture<T>> supplier) {
        Objects.requireNonNull(supplier);
        return Single.defer(() -> toSingle(supplier.get()));
    }

    /**
     * Convert a ListenableFuture to a Single
     * @param future the future
     * @param <T> the type returned by the future
     * @return the single
     */
    private static <T> Single<T> toSingle(ListenableFuture<T> future) {
        CompletableFuture<T> cf = future.toCompletableFuture();
        return Single.create((SingleEmitter<T> emitter) -> {
            cf.whenComplete((val, t) -> {
                if (t != null)
                    emitter.onError(unwrapException(t));
                else
                    emitter.onSuccess(val);
            });
            emitter.setCancellable(() -> cf.cancel(true));
        }).observeOn(Schedulers.computation());
    }

    private static Throwable unwrapException(Throwable t) {
        Throwable result = t;
        if (t instanceof ExecutionException && t.getCause() != null) {
            logger.debug("Unwrapping exception", t);
            result = t.getCause();
        }
        return result;
    }

    private AsyncHttpReactiveUtils() { }
}
