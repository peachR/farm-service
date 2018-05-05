package com.yiyi.farm.dao.invite;

import com.yiyi.farm.entity.invite.LogConsumeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ConsumeLogDao {
    List<LogConsumeEntity> findAll();

    Map<String,BigDecimal> findConsume(@Param("phone") String phone, @Param("startTime")int startTime,@Param("endTime") int endTime);
}
