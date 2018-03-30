package com.yiyi.farm.service.customer;

import com.yiyi.farm.dao.customer.CustomerDao;
import com.yiyi.farm.entity.customer.CustomerEntity;
import com.yiyi.farm.excpetion.CommonException;
import com.yiyi.farm.excpetion.ExpCodeEnum;
import com.yiyi.farm.facade.customer.CustomerInformationService;
import com.yiyi.farm.req.information.InformationReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInformationServiceImpl implements CustomerInformationService {
    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<CustomerEntity> findCustomerInfo() {
        return null;
    }

    @Override
    public List<CustomerEntity> findCustomerInfo(InformationReq infoReq) {
        checkParam(infoReq, ExpCodeEnum.INFORMATIONREQ_NULL);

        return customerDao.findCustomerInfo(infoReq);
    }

    private <T> void checkParam(T req, ExpCodeEnum expCodeEnum){
        if(req == null)
            throw new CommonException(expCodeEnum);
    }
}
