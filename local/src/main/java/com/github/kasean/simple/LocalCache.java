package com.github.kasean.simple;

import com.github.kasean.cache.Cache;

public interface LocalCache<K, V> extends Cache<K, V> {
    int getCacheSize();
}
