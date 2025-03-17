package com.github.kasean.simple;

import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheImpl<K, V> implements LocalCache<K, V>{

    private final ConcurrentHashMap<K, V> storage = new ConcurrentHashMap<>();

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
    }

    @Override
    public V get(K key) {
        return storage.get(key);
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }

    @Override
    public int getCacheSize() {
        return storage.size();
    }
}
