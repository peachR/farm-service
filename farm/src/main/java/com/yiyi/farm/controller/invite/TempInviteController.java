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

    /**
     * 重新缓存，弃用
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("initCache")
    public void initCache() throws ExecutionException, InterruptedException {
        inviteService.initCache();
    }

    /**
     * 获取统计信息，邀请人数，有效人数，消费信息
     * @param inviteReq
     * @return
     */
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

    /**
     * 测试遍历所有节点要多久，没啥用
     * @return
     */
    @GetMapping("test")
    public Integer test(){
        return inviteService.test().size();
    }

    /**
     * 获取邀请数Rank，所有号码 新增邀请数（包括子、孙、曾孙、到无穷） 的排名情况
     * @param inviteReq
     * @return
     */
    @GetMapping("rank")
    public Result<List<Map<String,String>>> getRankByMonth(InviteReq inviteReq){
        if(isBusy()){
            Result<List<Map<String,String>>> result = Result.newFailureResult();
            result.setErrorMsg("busy");
            return result;
        }
        return Result.newSuccessResult(inviteService.getRank(inviteReq).subList(0,inviteReq.getRankNumber()));
    }

    /**
     *获取邀请数Rank，所有号码 新增邀请数（包括子、孙、曾孙、到无穷） 的排名情况
     * @param inviteReq
     * @return 只返回号码
     */
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

    /**
     * 获取统计信息，邀请人数，充值信息
     * @return
     */
    @GetMapping("recharge")
    public Result<List<Map<String,Object>>> getRecharge(InviteReq inviteReq){
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
            map.put("level",inviteService.findRechargeStatistics(phone,inviteReq));
            result.add(map);
        }
        return Result.newSuccessResult(result);
    }

    /**
     * 判断当前系统是否正在建立关系树和缓存
     * @return
     */
    private boolean isBusy(){
        boolean hasKey = redisService.exist("busy");
        return hasKey;
    }
}
