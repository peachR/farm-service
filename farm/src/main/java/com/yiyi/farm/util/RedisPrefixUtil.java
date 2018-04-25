package com.yiyi.farm.util;

import com.yiyi.farm.facade.redis.RedisService;

/**
 * @author peach
 * @date 2018-04-25 15:12:15
 * @description redis储存时的前缀
 */
public final class RedisPrefixUtil {
    private RedisPrefixUtil(){}

    public static final String ACCESS_AUTH_PREFIX = "AUTH";
}
