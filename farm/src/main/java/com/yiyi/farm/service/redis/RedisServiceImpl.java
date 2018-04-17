package com.yiyi.farm.service.redis;

import com.yiyi.farm.facade.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void remove(String... keys) {
        for(String key : keys){
            remove(key);
        }
    }

    @Override
    public void removePattern(String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if(keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void remove(String key) {
        if(exist(key)){
            redisTemplate.delete(key);
        }
    }

    @Override
    public boolean exist(final String key){
        return redisTemplate.hasKey(key);
    }

    @Override
    public Serializable get(String key) {
        Serializable result = null;
        ValueOperations<Serializable,Serializable> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    @Override
    public boolean set(String key, Serializable value) {
        boolean result = false;
        try{
            ValueOperations<Serializable,Serializable> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean set(String key, Serializable value, long expireTime) {
        boolean result = false;
        try{
            ValueOperations<Serializable, Serializable> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public <K, HK, HV> boolean hmset(K key, Map<HK, HV> map, Long expireTime) {
        HashOperations<K,HK,HV> operations = redisTemplate.opsForHash();
        operations.putAll(key, map);

        if(expireTime != null){
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public <K, HK, HV> Map<HK, HV> hgetAll(K key) {
        HashOperations<K, HK, HV> operations = redisTemplate.opsForHash();
        return operations.entries(key);
    }
}
