package com.yiyi.farm.dao.invite;

import com.yiyi.farm.entity.invite.LogRechargeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2018-6-27.
 */
@Mapper
public interface RechargeLogDao {
    List<LogRechargeEntity> findAll();
}
