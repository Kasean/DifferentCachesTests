package com.github.kasean.simple;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LocalCacheImplTest {

    private final LocalCache<Integer, String> cache = new LocalCacheImpl<>();

    @Test
    void put() {
        Integer key = 1;
        String value = "value";

        cache.put(key, value);

        assertTrue(cache.getCacheSize() > 0);
        assertEquals(cache.getCacheSize(), 1);
    }

    @Test
    void get() {
        Integer key = 1;
        String value = "value";

        cache.put(key, value);

        assertTrue(cache.getCacheSize() > 0);
        assertEquals(cache.getCacheSize(), 1);

        var actualValue = cache.get(key);

        assertNotNull(actualValue);
        assertEquals(actualValue, value);
    }

    @Test
    void remove() {
        Integer key = 1;
        String value = "value";

        cache.put(key, value);

        assertTrue(cache.getCacheSize() > 0);
        assertEquals(cache.getCacheSize(), 1);

        cache.remove(key);

        assertEquals(cache.getCacheSize(), 0);
    }

    @Test
    public void testConcurrentPut() throws InterruptedException {
        int numberOfThreads = 10;
        int numberOfElements = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < numberOfElements; j++) {
                    cache.put(threadId * numberOfElements + j, "Value" + j);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(numberOfThreads * numberOfElements, cache.getCacheSize());
    }

    @Test
    public void testConcurrentGetAndPut() throws InterruptedException {
        int numberOfThreads = 10;
        int numberOfElements = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < numberOfElements; j++) {
                    cache.put(threadId * numberOfElements + j, "Value" + j);
                    cache.get(threadId * numberOfElements + j);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(numberOfThreads * numberOfElements, cache.getCacheSize());
    }

    @Test
    public void testConcurrentRemove() throws InterruptedException {
        LocalCacheImpl<Integer, String> cache = new LocalCacheImpl<>();
        int numberOfThreads = 10;
        int numberOfElements = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads * numberOfElements; i++) {
            cache.put(i, "Value" + i);
        }

        assertEquals(numberOfThreads  * numberOfElements, cache.getCacheSize());

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < numberOfElements; j++) {
                    cache.remove(threadId * numberOfElements + j);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(cache.getCacheSize(), 0);
    }

    @Test
    public void testConcurrentMixedOperations() throws InterruptedException {
        int numberOfThreads = 10;
        int numberOfElements = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < numberOfElements; j++) {
                    cache.put(threadId * numberOfElements + j, "Value" + j);
                    cache.get(threadId * numberOfElements + j);
                    cache.remove(threadId * numberOfElements + j);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(0, cache.getCacheSize());
    }
}