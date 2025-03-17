package com.github.kasean.guava;

import com.github.kasean.guava.cache.GuavaLocalCache;
import com.github.kasean.guava.cache.GuavaLocalCacheImpl;
import com.github.kasean.guava.config.GuavaCacheConfig;
import com.google.common.cache.CacheLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuavaLocalCacheImplTest {

    GuavaLocalCache<String, String> cache;

    @BeforeEach
    void setUp() {
        GuavaCacheConfig config = new GuavaCacheConfig(10, 1);

        CacheLoader<String, String> loader = CacheLoader.from(key -> "Value for key " + key);

        cache = new GuavaLocalCacheImpl<>(config, loader);
    }

    @Test
    void put() {
        var key = "key";
        var value = "value";

        cache.put(key, value);

        assertNotNull(cache.get(key));
        assertEquals(cache.get(key), value);
    }

    @Test
    void get() {
        var key1 = "key";
        var value1 = "value";

        cache.put(key1, value1);

        assertNotNull(cache.get(key1));
        assertEquals(cache.get(key1), value1);

        var key2 = "second key";
        var expectedValue2 = "Value for key second key";

        assertNotNull(cache.get(key2));
        assertEquals(cache.get(key2), expectedValue2);
    }

    @Test
    void remove() {
        var key = "key";
        var value = "value";
        var expectedValue2 = "Value for key " + key;

        cache.put(key, value);

        assertNotNull(cache.get(key));
        assertEquals(cache.get(key), value);

        cache.remove(key);

        assertNotNull(cache.get(key));
        assertNotEquals(cache.get(key), value);
        assertEquals(cache.get(key), expectedValue2);
    }

    @Test
    void expiration() throws InterruptedException {
        var key = "key";
        var value = "value";

        cache.put(key, value);

        assertNotNull(cache.get(key));
        assertEquals(cache.get(key), value);

        Thread.sleep((1000 * 60) + 10);

        var expectedValue2 = "Value for key " + key;

        assertNotNull(cache.get(key));
        assertNotEquals(cache.get(key), value);
        assertEquals(cache.get(key), expectedValue2);
    }
}