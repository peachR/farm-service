package com.yiyi.farm.controller.invite;

import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.facade.invite.InviteService;
import com.yiyi.farm.req.invite.InviteReq;
import com.yiyi.farm.rsp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class InviteControllerImpl implements InviteController {
    ThreadPoolExecutor singlePool = new ThreadPoolExecutor(1,1,0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    @Autowired
    private InviteService inviteService;

    @Override
    public Result handleInit() {
        singlePool.execute(() -> inviteService.init());

        return Result.newSuccessResult();
    }

    @Override
    public Result handleFindChildByPhone(InviteReq invite) {
        Queue<InviteRelationEntity> list = inviteService.findChildByPhone(invite.getPhone());
        return Result.newSuccessResult(list);
    }

    @Override
    public Result handleFindChildByUid(InviteReq invite) {
        Queue<InviteRelationEntity> list = inviteService.findChildByPhone(invite.getUid());
        return Result.newSuccessResult(list);
    }

}
