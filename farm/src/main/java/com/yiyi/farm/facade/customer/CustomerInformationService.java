package com.yiyi.farm.facade.customer;


import com.yiyi.farm.entity.customer.CustomerEntity;
import com.yiyi.farm.req.information.InformationReq;

import java.util.List;

public interface CustomerInformationService {
    List<CustomerEntity> findCustomerInfo();

    List<CustomerEntity> findCustomerInfo(InformationReq infoReq);

    CustomerEntity getCustInfoByPhone(String phone);

}
