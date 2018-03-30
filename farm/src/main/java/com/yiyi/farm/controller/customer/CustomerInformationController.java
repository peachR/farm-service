package com.yiyi.farm.controller.customer;

import com.yiyi.farm.annotation.Login;
import com.yiyi.farm.entity.customer.CustomerEntity;
import com.yiyi.farm.req.information.InformationReq;
import com.yiyi.farm.rsp.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author peach
 * @date 2018-03-30 10:35:55
 * @description 客户控制器
 */
@RestController
@RequestMapping(value="/customer")
public interface CustomerInformationController {

    /**
     * 获取所有客户
     * @return
     */
    @Login
    @GetMapping("/allInfo")
    public Result<List<CustomerEntity>> findInformationForAll();

    /**
     * 获取指定页数的用户信息
     * @param inforeq
     * @return
     */
    @Login
    @GetMapping("/info")
    public Result<List<CustomerEntity>> findInformationInPage(InformationReq inforeq);


}
