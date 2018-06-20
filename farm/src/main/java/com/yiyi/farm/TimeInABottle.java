package com.yiyi.farm;

import com.yiyi.farm.facade.redis.RedisService;
import com.yiyi.farm.service.invite.InviteServiceImpl;
import com.yiyi.farm.service.invite.TempInviteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Scheduled(cron = " 0 0 2 * * ?")
    public void rebuildTreeAndCache() throws ExecutionException, InterruptedException {
        redisService.set("busy","yes");
        inviteService.init();
        tempInviteServiceinviteService.initCache();
        redisService.remove("busy");
    }


}
