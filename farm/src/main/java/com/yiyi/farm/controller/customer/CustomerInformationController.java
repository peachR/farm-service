package com.yiyi.farm.controller.customer;

import com.yiyi.farm.annotation.Login;
import com.yiyi.farm.entity.customer.CustomerEntity;
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
    public List<CustomerEntity> findInformationForAll();

    /**
     * 获取指定页数的用户信息
     * @param page 获取页数
     * @param items 每页最大个数
     * @return
     */
    @Login
    @GetMapping("/info")
    public List<CustomerEntity> findInformationInPage(@RequestParam(value="page") int page, @RequestParam(value = "items", required = false, defaultValue = "10") int items);

}
