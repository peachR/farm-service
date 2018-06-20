package com.yiyi.farm.controller.invite;

import com.yiyi.farm.facade.redis.RedisService;
import com.yiyi.farm.req.invite.InviteReq;
import com.yiyi.farm.rsp.Result;
import com.yiyi.farm.service.invite.TempInviteServiceImpl;
import com.yiyi.farm.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2018-5-15.
 */
@RestController
public class TempInviteController {

    @Autowired
    private TempInviteServiceImpl inviteService;

    @Autowired
    private RedisService redisService;

    @PostMapping("initCache")
    public void initCache() throws ExecutionException, InterruptedException {
        inviteService.initCache();
    }

    @GetMapping("statistics")
    public Result<List<Map<String,Object>>> getStatistics(InviteReq inviteReq){
        if(isBusy()){
            Result<List<Map<String,Object>>> result = Result.newFailureResult();
            result.setErrorMsg("busy");
            return result;
        }
        String[] phones = StringUtil.split(inviteReq.getPhone(),";");
        List<Map<String,Object>> result = new ArrayList<>();
        for (String phone:phones) {
            Map<String,Object> map = new HashMap<>();
            map.put("phone",phone);
            map.put("level",inviteService.findStatistics(phone,inviteReq));
            result.add(map);
        }
        return Result.newSuccessResult(result);
    }
    @GetMapping("test")
    public Integer test(){
        return inviteService.test().size();
    }

    @GetMapping("rank")
    public Result<List<Map<String,String>>> getRankByMonth(InviteReq inviteReq){
        if(isBusy()){
            Result<List<Map<String,String>>> result = Result.newFailureResult();
            result.setErrorMsg("busy");
            return result;
        }
        return Result.newSuccessResult(inviteService.getRank(inviteReq).subList(0,inviteReq.getRankNumber()));
    }

    @GetMapping("rankOnlyPhone")
    public Result<List<String>> getRankByMonthOnlyPhone(InviteReq inviteReq){
        if(isBusy()){
            Result<List<String>> result = Result.newFailureResult();
            result.setErrorMsg("busy");
            return result;
        }
        List<Map<String,String>> result = inviteService.getRank(inviteReq).subList(0,inviteReq.getRankNumber());
        List<String> phones = new ArrayList<>();
        for(Map<String,String> map : result){
            phones.add(map.get("phone"));
        }
        return Result.newSuccessResult(phones);
    }

    private boolean isBusy(){
        boolean hasKey = redisService.exist("busy");
        return hasKey;
    }
}
