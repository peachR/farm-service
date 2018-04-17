package com.yiyi.farm.facade.redis;

import java.io.Serializable;
import java.util.Map;

/**
 * @author peach
 * @date 2018-04-11 13:54:51
 * @description redis服务接口
 */
public interface RedisService {
    /**
     * 批量删除
     * @param keys
     */
    void remove(final String ...keys);

    /**
     * 删除指定key
     * @param pattern
     */
    void removePattern(final String pattern);

    /**
     * 删除指定key
     * @param key
     */
    void remove(final String key);

    /**
     * 检查key是否存在
     * @param key
     * @return
     */
    boolean exist(final String key);

    /**
     * 获取指定对象
     * @param key
     * @return
     */
    Serializable get(final String key);

    /**
     * 添加key-value,使用默认的过期时间
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, Serializable value);

    /**
     * 添加key-value,使用指定过期时间
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    boolean set(final String key, Serializable value, long expireTime);


    <K,HK,HV> boolean hmset(K key, Map<HK, HV> map, Long expireTime);

    <K,HK,HV> Map<HK,HV> hgetAll(final K key);
}
