package com.yiyi.farm;

import com.yiyi.farm.facade.redis.RedisService;
import com.yiyi.farm.service.invite.InviteServiceImpl;
import com.yiyi.farm.service.invite.TempInviteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * 定时任务
 */
@Component
@EnableScheduling
public class TimeInABottle {
    @Autowired
    private TempInviteServiceImpl tempInviteServiceinviteService;

    @Autowired
    private InviteServiceImpl inviteService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = " 0 0 23 * * ?")
    public void rebuildTreeAndCache() throws ExecutionException, InterruptedException {
        flushDB();
        redisService.set("busy","yes");
        inviteService.newInit();
        //tempInviteServiceinviteService.initCache();
        redisService.remove("busy");
    }

    private void flushDB(){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.flushDb();
                return "";
            }
        });
    }
}
