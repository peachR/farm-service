package com.yiyi.farm.controller.user;

import com.yiyi.farm.req.user.LoginReq;
import com.yiyi.farm.rsp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public interface UserController {
    @GetMapping("/login")
    Result login(LoginReq req, HttpServletResponse rsq);
}
