package com.yiyi.farm.facade.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author peach
 * @date 2018-04-11 13:54:51
 * @description redis服务接口
 */
public interface RedisService {
    /**
     * 批量删除
     * @param keys 待删除项的键值
     */
    void remove(final String ...keys);

    /**
     * 删除指定key
     * @param pattern 待删除集合的键值表达式，可含有通配符(*匹配多个字符，?匹配一个字符，[]匹配指定字符)
     */
    void removePattern(final String pattern);

    /**
     * 删除指定key
     * @param key 带删除项的键
     */
    void remove(final String key);

    /**
     * 检查key是否存在
     * @param key
     * @return
     */
    boolean exist(final String key);

    /**
     * 根据key获取指定对象
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

    /**
     * 添加一个hash表
     * @param key 存在redis中的哈希表的键
     * @param map 哈希表
     * @param expireTime 过期时间
     * @param <K> 键的类型
     * @param <HK> 哈希表键的类型
     * @param <HV> 哈希表值得类型
     * @return
     */
    <K,HK,HV> boolean hmset(K key, Map<HK, HV> map, Long expireTime);

    /**
     * @see com.yiyi.farm.facade.redis.RedisService#hmset(Object, Map, Long)
     * @param key
     * @param map
     * @param <K>
     * @param <HK>
     * @param <HV>
     * @return
     */
    <K,HK,HV> boolean hmset(K key, Map<HK, HV> map);

    /**
     * 获取一个缓存的hash表
     * @param key redis中哈希表的键
     * @param <K> 键的类型
     * @param <HK> 哈希表键的类型
     * @param <HV> 哈希表值得类型
     * @return
     */
    <K,HK,HV> Map<HK,HV> hgetAll(final K key);

    /**
     * 缓存一个列表
     * @param key 列表的键值
     * @param expireTime 过期时间
     * @param values 要缓存在本列表中的值
     * @param <K> 键的类型
     * @param <LV> 值得类型
     * @return
     */
    <K, LV> boolean leftPushAll(K key, Long expireTime, LV ...values);

    /**
     * 缓存一个列表
     * @param key 列表的键值
     * @param expireTime 过期时间
     * @param values 要缓存在本列表中的值得集合
     * @param <K> 键的类型
     * @param <LV> 值得类型
     * @return
     */
    <K, LV> boolean leftPushAll(K key, Collection<LV> values, Long expireTime);

    /**
     * 缓存一个列表，无过期时间
     * @param key 列表的键值
     * @param values 要缓存在本列表中的值
     * @param <K> 键的类型
     * @param <LV> 值得类型
     * @return
     */
    <K, LV> boolean leftPushAll(K key, Collection<LV> values);

    /**
     * 获取一个列表的某个范围中的值
     * @param key 列表键的值
     * @param fromIndex 要获取的列表范围的起始下标
     * @param toIndex 要获取的列表范围的结束下标
     * @param <K> 键的类型
     * @param <LV> 值的类型
     * @return
     */
    <K, LV> List<LV> lRange(K key, long fromIndex, long toIndex);

    /**
     * 获取一个列表的全部内容
     * @param key 列表键的值
     * @param <K> 键的类型
     * @param <LV> 值的类型
     * @return
     */
    <K, LV> List<LV> lGetAll(K key);
}
