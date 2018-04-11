package com.yiyi.farm.controller.user;

import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.req.user.LoginReq;
import com.yiyi.farm.rsp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserControllerImpl implements UserController {

    @Value("${session.expireTime}")
    private long sessionExpireTime;

    @Value("${session.sessionIdName}")
    private String sessionIdName;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result login(LoginReq req, HttpServletResponse rsq) {
        InviteInfoEntity entity = new InviteInfoEntity();
        entity.setHigh(10);
        entity.setPhone("11011011000");
        entity.setUpPlayerPhone("99999999999");
        entity.setServerId(1);
        redisTemplate.opsForValue().set("redistest:user2",entity);
        InviteInfoEntity r = (InviteInfoEntity) redisTemplate.opsForValue().get("redistest:user2");
        System.out.println(entity);
        System.out.println("in");
        return Result.newSuccessResult(r);
    }
}
