package com.yiyi.farm.controller.invite;

import com.yiyi.farm.req.invite.InviteReq;
import com.yiyi.farm.rsp.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;

@RestController
@RequestMapping(value="/invite")
public interface InviteController {
    @PutMapping("init")
    public Result handleInit();

    @GetMapping("childByPhone")
    public Result handleFindChildByPhone(InviteReq invite);

    @GetMapping("childNumberByPhone")
    public Result handleFindChildNumberByPhone(InviteReq inviteReq);

    @GetMapping("childByUid")
    public Result handleFindChildByUid(InviteReq invite);

    @GetMapping("posterityStatistics")
    Result handleposterityStatistics(InviteReq invite);

    @GetMapping("redEnvelope")
    Result handleredEnvelopeCalc(InviteReq invite);

    @GetMapping("refreshTime")
    Result handleFindRefreshTime();

    @PostMapping("Cache")
    Result handleAddCache();
}
