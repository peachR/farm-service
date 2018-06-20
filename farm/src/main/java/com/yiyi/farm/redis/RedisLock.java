package com.yiyi.farm.redis;

import com.yiyi.farm.facade.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLock {
    private final static long DEFAULT_WAIT_TIME = 100;
    private final static long DEFAULT_EXPIRE_TIME = 1000;
    private final static String LOCK_PREFIX = "lock:";

    @Autowired
    private RedisService redisService;

    public String lock(String key){
        return lock(key, DEFAULT_WAIT_TIME);
    }

    public String lock(String key, long waitTime){
        String uuid = UUID.randomUUID().toString();

        long endTime = System.nanoTime() + waitTime;

        while(System.nanoTime() < endTime){
            if(redisService.setIfAbsent(LOCK_PREFIX + key, uuid)){
                return uuid;
            }
        }
        return null;
    }

    public String tryLock(String key){
        String uuid = UUID.randomUUID().toString();
        if(redisService.setIfAbsent(LOCK_PREFIX + key, uuid)){
            return uuid;
        }
        return null;
    }

    public String lockExpire(String key, long expire, TimeUnit unit){
        return lockExpire(key, expire, unit, DEFAULT_WAIT_TIME);
    }

    public String lockExpire(String key, long expire, TimeUnit unit, long waitTime){
        String uuid = UUID.randomUUID().toString();

        long endTime = System.nanoTime() + waitTime;

        while (System.nanoTime() < endTime){
            if(redisService.setIfAbsent(key, uuid)){
                redisService.setExpire(key, unit.toMillis(expire));
                return uuid;
            }

            if(redisService.getExpire(key) == -1){
                redisService.setExpire(key, unit.toMillis(expire));
            }

        }
        return null;
    }

    public boolean unlock(String key, String uuid){
        List<Object> result;
        while (true){
            redisService.watch(key);
            if(uuid.equals(redisService.get(key))) {
                result = redisService.doTransaction(() -> {
                    redisService.remove(key);
                });
                if(result == null) {
                    continue;
                }
                return true;
            }
            redisService.unwatch();
            return false;
        }
    }
}
