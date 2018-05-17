package com.yiyi.farm.controller.invite;

import com.yiyi.farm.req.invite.InviteReq;
import com.yiyi.farm.rsp.Result;
import com.yiyi.farm.service.invite.TempInviteServiceImpl;
import com.yiyi.farm.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-5-15.
 */
@RestController
public class TempInviteController {

    @Autowired
    private TempInviteServiceImpl inviteService;

    @PostMapping("initCache")
    public void initCache(){
        inviteService.initCache();
    }

    @GetMapping("statistics")
    public Result<List<Map<String,Map<String,TempInviteServiceImpl.ChildStatistics>>>> getStatistics(InviteReq inviteReq){

        String[] phones = StringUtil.split(inviteReq.getPhone(),",");
        List<Map<String,Map<String,TempInviteServiceImpl.ChildStatistics>>> result = new ArrayList<>();
        long start = System.nanoTime();
        for (String phone:phones) {
            Map<String,Map<String,TempInviteServiceImpl.ChildStatistics>> map = new HashMap<>();
            map.put(phone,inviteService.findStatistics(phone,inviteReq));
            result.add(map);
        }

        return Result.newSuccessResult(result);
    }
}
