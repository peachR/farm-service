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
    public Result<List<Map<String,Object>>> getStatistics(InviteReq inviteReq){

        String[] phones = StringUtil.split(inviteReq.getPhone(),",");
        List<Map<String,Object>> result = new ArrayList<>();
        for (String phone:phones) {
            Map<String,Object> map = new HashMap<>();
            map.put("phone",phone);
            map.put("level",inviteService.findStatistics(phone,inviteReq));
            result.add(map);
        }

        return Result.newSuccessResult(result);
    }
}
