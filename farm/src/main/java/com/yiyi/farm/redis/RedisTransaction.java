package com.yiyi.farm.redis;

@FunctionalInterface
public interface RedisTransaction {
    void transaction();
}
