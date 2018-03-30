package com.yiyi.farm.controller.customer;

import com.yiyi.farm.entity.customer.CustomerEntity;
import com.yiyi.farm.excpetion.CommonException;
import com.yiyi.farm.excpetion.ExpCodeEnum;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerInformationControllerImpl implements CustomerInformationController {
    @Override
    public List<CustomerEntity> findInformationForAll() {
        throw new CommonException(ExpCodeEnum.UNKNOW_ERROR);
    }

    @Override
    public List<CustomerEntity> findInformationInPage(int page) {
        System.out.println(page);
        return null;
    }
}
