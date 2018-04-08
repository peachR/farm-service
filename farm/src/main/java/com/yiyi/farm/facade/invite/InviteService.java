package com.yiyi.farm.facade.invite;

import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;

import java.util.List;
import java.util.Map;

public interface InviteService {
    void init();
    List<InviteRelationEntity> findChildByPhone(String phone);
    List<InviteRelationEntity> findChildByUid(String uid);
}
