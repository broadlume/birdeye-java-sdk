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
