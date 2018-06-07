package com.yiyi.farm.controller.invite;

import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.facade.invite.InviteService;
import com.yiyi.farm.req.invite.InviteReq;
import com.yiyi.farm.rsp.Result;
import com.yiyi.farm.tool.Pair;
import com.yiyi.farm.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

@RestController
public class InviteControllerImpl implements InviteController {
    @Autowired
    private ThreadPoolExecutor initEventProcessThreadPool;

    @Autowired
    private InviteService inviteService;

    @Override
    public Result handleInit() {
        initEventProcessThreadPool.execute(() -> inviteService.init());

        return Result.newSuccessResult();
    }

    @Override
    public Result handleFindChildByPhone(InviteReq invite) {
        String[] phones = StringUtil.split(invite.getPhone(),";");
        Queue<InviteRelationEntity> list = inviteService.findChildByPhone(phones[0]);
        return Result.newSuccessResult(list);
    }

    @Override
    public Result handleFindChildNumberByPhone(InviteReq invite) {
        String[] phones = StringUtil.split(invite.getPhone(),";");
        Map<String, Pair> map = new ConcurrentHashMap<>();
        Stream.of(phones).parallel().forEach(phone -> map.put(phone, inviteService.findChildNumbersByPhone(phone)));
        System.out.println(map);
        return Result.newSuccessResult(map);
    }

    @Override
    public Result handleFindChildByUid(InviteReq invite) {
        String[] uids = invite.getUid();
        Queue<InviteRelationEntity> list = inviteService.findChildByUid(uids[0]);
        return Result.newSuccessResult(list);
    }

    @Override
    public Result handleposterityStatistics(InviteReq invite) {
        String[] phones = StringUtil.split(invite.getPhone(),";");
        return Result.newSuccessResult(inviteService.findRelationNumberMap(phones[0],invite.getTotalConsume(),invite.getChargeConsume()));
    }

    @Override
    @Cacheable(cacheNames = "envelopCalc")
    public Result handleredEnvelopeCalc(InviteReq invite) {
        String[] phones = StringUtil.split(invite.getPhone(),";");
        List<Map<String,String>> result = new ArrayList<>();
        long start = System.nanoTime();
        for (String phone:phones) {
            result.add(inviteService.findRedEnvelopeCalcParallel(phone,invite));
        }
        System.out.println("time is :" + ((System.nanoTime() - start)/ 1_000_000));
        System.out.println("no cache");
        return Result.newSuccessResult(result);
    }

    @Override
    public Result handleFindRefreshTime() {
        return Result.newSuccessResult(inviteService.findRefreshTime());
    }

    @Override
    public Result handleAddCache() {
        initEventProcessThreadPool.execute(() -> inviteService.initCaching());
        return Result.newSuccessResult();
    }

}
