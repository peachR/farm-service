package com.yiyi.farm.facade.invite;

import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.tool.Pair;
import com.yiyi.farm.tool.PosterityStatistics;

import javax.websocket.Session;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executor;

public interface InviteService {
    void init();
    Queue<InviteRelationEntity> findChildByPhone(String phone, int[]first);
    Queue<InviteRelationEntity> findChildByUid(String uid, int[] first);
    Queue<InviteRelationEntity> findChildByPhone(String phone);
    Queue<InviteRelationEntity> findChildByUid(String uid);
    Pair findChildNumbersByPhone(String phone);
    List<PosterityStatistics> findRelationNumberMap(String phone,int totalConsume,int chargeConsume);
    Map<String,String> findRedEnvelopeCalc(String phone, int startTime, int endTime, int totalConsume, int chargeConsume);
    List<String> findRefreshTime();
}
