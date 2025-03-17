package com.github.kasean.guava.cache;

import com.github.kasean.guava.config.GuavaCacheConfig;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaLocalCacheImpl<K, V> implements GuavaLocalCache<K, V> {

    public final LoadingCache<K, V> cache;

    public GuavaLocalCacheImpl(GuavaCacheConfig config, CacheLoader<K, V> loader) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(config.getMaxSize())
                .expireAfterWrite(config.getExpireTime(), TimeUnit.MINUTES)
                .build(loader);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(K key) {
        cache.invalidate(key);
    }
}
