package com.yiyi.farm.dao.invite;

import com.yiyi.farm.entity.invite.InviteInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface InviteInfoDao {
    public ArrayList<InviteInfoEntity> findRoots();

    public List<InviteInfoEntity> findChilds(@Param("uid")String uid);

    public List<InviteInfoEntity> findAll();
}
