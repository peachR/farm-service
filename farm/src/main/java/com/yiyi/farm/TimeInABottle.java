package com.yiyi.farm;

import com.yiyi.farm.service.invite.InviteServiceImpl;
import com.yiyi.farm.service.invite.TempInviteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Scheduled(cron = " 0 0 2 * * ?")
    public void rebuildTreeAndCache() throws ExecutionException, InterruptedException {
        inviteService.init();
        tempInviteServiceinviteService.initCache();
    }


}
