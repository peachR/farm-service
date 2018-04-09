package com.yiyi.farm.dao.customer;

import com.yiyi.farm.entity.customer.CustomerEntity;
import com.yiyi.farm.req.information.InformationReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerDao {
    public List<CustomerEntity> findCustomerInfo(@Param("infoReq")InformationReq infoReq);

    Integer getServerOfCust(String phone);

    CustomerEntity getCustFromServer1(String phone);

    CustomerEntity getCustFromServer2(String phone);
}
