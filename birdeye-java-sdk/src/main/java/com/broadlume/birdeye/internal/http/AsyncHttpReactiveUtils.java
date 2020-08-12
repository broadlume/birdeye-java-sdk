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
