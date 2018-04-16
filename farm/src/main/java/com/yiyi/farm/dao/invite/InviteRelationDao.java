package com.yiyi.farm.dao.invite;

import com.yiyi.farm.entity.invite.InviteRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface InviteRelationDao {
    public void insertRelation(@Param("relationList")List<InviteRelationEntity> relation);
    public List<InviteRelationEntity> findChildByPhone(@Param("phone") String phone);
    public List<InviteRelationEntity> findChildByUid(@Param("uid") String uid);
    List<InviteRelationEntity> findChildrenByPhone(@Param("phone") String phone);
    Map<String,BigDecimal> checkValidCustomer(@Param("phone") String phone);
    void clearRelation();
    Integer recordRefreshTime();
}
