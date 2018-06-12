package com.yiyi.farm.dao.invite;

import com.yiyi.farm.entity.invite.InviteRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
public interface InviteRelationDao {
    int checkUnregister(@Param("phone")String phone);
    void insertRelation(@Param("relationList")List<InviteRelationEntity> relation);
    List<InviteRelationEntity> findAllRelation();
    List<InviteRelationEntity> findChildByPhone(@Param("phone") String phone);
    List<InviteRelationEntity> findChildByUid(@Param("uid") String uid);
    List<InviteRelationEntity> findChildrenByPhone(@Param("phone") String phone);
    Map<String,BigDecimal> checkValidCustomer(@Param("phone") String phone);
    void clearRelation();
    Integer recordRefreshTime();
    List<InviteRelationEntity> findChildrenByPhoneWithTime(@Param("phone") String phone, @Param("startTime")int startTime,@Param("endTime") int endTime);
    Integer findTotalCharge(@Param("phone") String phone, @Param("startTime")int startTime,@Param("endTime") int endTime);
    List<String> findRefreshTime();
    Integer isNewCustomer(@Param("phone") String phone, @Param("startTime")int startTime,@Param("endTime") int endTime);    //判断是否该时间段内新增的用户
}
