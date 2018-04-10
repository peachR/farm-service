package com.yiyi.farm.controller.customer;

import com.yiyi.farm.entity.customer.CustomerEntity;
import com.yiyi.farm.excpetion.CommonException;
import com.yiyi.farm.excpetion.ExpCodeEnum;
import com.yiyi.farm.facade.customer.CustomerInformationService;
import com.yiyi.farm.req.information.InformationReq;
import com.yiyi.farm.rsp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author peach
 * @date 2018-03-30 15:17:33
 * @description
 */
@RestController
public class CustomerInformationControllerImpl implements CustomerInformationController {

    @Autowired
    private CustomerInformationService infoService;

    @Override
    public Result<List<CustomerEntity>> findInformationForAll() {
        throw new CommonException(ExpCodeEnum.UNKNOW_ERROR);
    }

    @Override
    public Result<List<CustomerEntity>> findInformationInPage(InformationReq inforeq) {
        List<CustomerEntity> infoList = infoService.findCustomerInfo(inforeq);
        return Result.newSuccessResult(infoList);
    }

    @Override
    public Result<CustomerEntity> findCustInfoByPhone(@PathVariable("phone") String phone) {
        CustomerEntity customer = infoService.getCustInfoByPhone(phone);
        return Result.newSuccessResult(customer);
    }
}
