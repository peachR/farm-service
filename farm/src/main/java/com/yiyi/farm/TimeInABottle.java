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
 * 定时任务，类名字瞎起的，Time in a bottle是x战警逆转未来的插曲,当时快银在秀操作
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

    /**
     * 定时在每天的6:30重新建立关系树和缓存。由于不知道为啥的bug,必须每天重启后台程序，所以服务器设置了每天6点重启后台，两个时间注意先后
     * 改Scheduled(cron = " 0 30 6 * * ?")来改时间，具体百度springboot 定时任务cron表达式
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Scheduled(cron = " 0 30 6 * * ?")
    public void rebuildTreeAndCache() throws ExecutionException, InterruptedException {
        flushDB();
        redisService.set("busy","yes",86400);
        inviteService.newInit();
        //tempInviteServiceinviteService.initCache();
        redisService.remove("busy");
    }

    /**
     * 清空redis
     */
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
