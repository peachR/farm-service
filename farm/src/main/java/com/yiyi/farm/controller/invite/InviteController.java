package com.yiyi.farm.controller.invite;

import com.yiyi.farm.req.invite.InviteReq;
import com.yiyi.farm.rsp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/invite")
public interface InviteController {
    @PutMapping("init")
    public Result handleInit();

    @GetMapping("childByphone")
    public Result handleFindChildByPhone(InviteReq invite);

    @GetMapping("childByUid")
    public Result handleFindChildByUid(InviteReq invite);
}
