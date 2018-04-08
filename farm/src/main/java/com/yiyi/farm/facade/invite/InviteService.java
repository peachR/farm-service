package com.yiyi.farm.facade.invite;

import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executor;

public interface InviteService {
    void init();
    Queue<InviteRelationEntity> findChildByPhone(String phone);
    Queue<InviteRelationEntity> findChildByUid(String uid);
}
