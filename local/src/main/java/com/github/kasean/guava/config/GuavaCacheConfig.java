package com.github.kasean.guava.config;

public class GuavaCacheConfig {

    private int maxSize;

    private long expireTime;

    public GuavaCacheConfig(int maxSize, int expireTime) {
        this.maxSize = maxSize;
        this.expireTime = expireTime;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
