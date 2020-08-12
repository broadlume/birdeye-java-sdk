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

import org.asynchttpclient.ListenableFuture;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class AsyncHttpReactiveUtilsTest {

    private CompletableFuture<String> cf = new CompletableFuture<>();
    private ListenableFuture<String> future = mock(ListenableFuture.class);

    @Before
    public void setup() {
        when(future.toCompletableFuture()).thenReturn(cf);
    }

    /**
     * Should convert the ListenableFuture to a Single and properly signal the result
     */
    @Test
    public void toSingleTest() throws InterruptedException {
        cf.complete("test-val");
        AsyncHttpReactiveUtils.toSingle(() -> future)
                .test().await()
                .assertResult("test-val");
    }

    /**
     * Should properly signal errors
     */
    @Test
    public void toSingleErrorTest() throws InterruptedException {
        cf.completeExceptionally(new RuntimeException("test"));
        AsyncHttpReactiveUtils.toSingle(() -> future)
                .test().await()
                .assertError(e -> e.getClass().equals(RuntimeException.class) && "test".equals(e.getMessage()));
    }

    /**
     * Should properly unwrap execution exceptions
     */
    @Test
    public void toSingleExecutionErrorTest() throws InterruptedException {
        cf.completeExceptionally(new ExecutionException(new RuntimeException("real exception")));
        AsyncHttpReactiveUtils.toSingle(() -> future)
                .test().await()
                .assertError(e -> e.getClass().equals(RuntimeException.class) && "real exception".equals(e.getMessage()));
    }
}
