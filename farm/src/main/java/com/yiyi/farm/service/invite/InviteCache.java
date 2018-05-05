package com.yiyi.farm.service.invite;


import com.yiyi.farm.dao.invite.ConsumeLogDao;
import com.yiyi.farm.dao.invite.InviteInfoDao;
import com.yiyi.farm.dao.invite.InviteRelationDao;
import com.yiyi.farm.entity.invite.Customerable;
import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.entity.invite.LogConsumeEntity;
import com.yiyi.farm.facade.redis.RedisService;
import com.yiyi.farm.util.CollectionUtil;
import com.yiyi.farm.util.generator.ConsumeGenerator;
import com.yiyi.farm.util.generator.InfoGenerator;
import com.yiyi.farm.util.generator.KeyGenerator;
import com.yiyi.farm.util.generator.RelationGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peach
 * @date 2018-05-04 21:47:41
 * @description Invite有关信息操作的缓存处理操作
 */
@Component
public class InviteCache {

    @Autowired
    private InviteInfoDao inviteInfoDao;

    @Autowired
    private ConsumeLogDao consumeLogDao;

    @Autowired
    private InviteRelationDao inviteRelationDao;

    @Autowired
    RedisService redisService;

    /**
     * 清除缓存
     * @return
     */
    public boolean clearCache(){
        List<? extends KeyGenerator> generators = Arrays.asList(ConsumeGenerator.getInstance(), InfoGenerator.getInstance(), RelationGenerator.getInstance());
        for(KeyGenerator generator : generators){
            redisService.removePattern(buildRemovePatternValue(generator.getPrefix()));
        }
        return true;
    }

    /**
     * 缓存Invite_info表，按照电话号码分类
     * @return
     */
    public boolean cacheInviteInfo(){
        List<InviteInfoEntity> allInfo = inviteInfoDao.findAll();
        groupAndCachingList(allInfo, InfoGenerator.getInstance());
        return true;
    }

    /**
     * 缓存Invite_info表，按照电话号码分类
     * @return
     */
    public boolean cacheLogConsume(){
        List<LogConsumeEntity> allConsume = consumeLogDao.findAll();
        groupAndCachingList(allConsume, ConsumeGenerator.getInstance());
        return true;
    }

    /**
     * 缓存邀请关系
     * @return
     */
    public boolean cahceInviteRelation(){
        List<InviteRelationEntity> allRelation = inviteRelationDao.findAllRelation();
        groupAndCachingList(allRelation, RelationGenerator.getInstance());
        return true;
    }

    /**
     * 从缓存中读取邀请信息数据，判断是否是新用户
     * @param phone
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean isNewCustomer(String phone, int startTime, int endTime){
        String key = InfoGenerator.getInstance().getKey(phone);
        List<InviteInfoEntity> customer = redisService.lGetAll(key);
        if(CollectionUtil.isEmpty(customer))
            return false;
        for(InviteInfoEntity entity: customer){
            if(entity.timeBetween(startTime,endTime))
                return true;
        }
        return false;
    }

    /**
     * 从缓存的消费日志中，查询消费和余额总额
     * @param phone
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Integer> findConsume(String phone, int startTime, int endTime){
        String key = ConsumeGenerator.getInstance().getKey(phone);
        List<LogConsumeEntity> customer = redisService.lGetAll(key);
        Map<String, Integer> result = new HashMap<>();
        int charge = 0, total = 0;
        for(LogConsumeEntity logConsumeEntity: customer){
            total += logConsumeEntity.getMoney();
            charge += logConsumeEntity.getYuer();
        }
        result.put("total", total);
        result.put("charge", charge);
        return result;
    }

    /**
     * 从缓存的邀请关系中，根据电话号码查询所有直接子节点
     * @param phone
     * @return
     */
    public List<InviteRelationEntity> findChildByPhone(String phone){
        String key = RelationGenerator.getInstance().getKey(phone);
        return redisService.lGetAll(key);
    }


    /**
     * 缓存列表
     * @param list 待缓存列表
     */
    private void groupAndCachingList(List<? extends Customerable> list, KeyGenerator generator) {
        Set<Map.Entry<String, List<Customerable>>> sets = groupingByPhone(list).entrySet();
        for(Map.Entry<String, List<Customerable>> entry: sets){
            String key = generator.getKey(entry.getKey());
            redisService.leftPushAll(key, entry.getValue());
        }
    }

    /**
     * 将集合按照手机分类
     * @param allInfo
     * @return
     */
    private Map<String, List<Customerable>> groupingByPhone(List<? extends Customerable> allInfo) {
        return allInfo.stream().collect(Collectors.
                groupingBy(info -> info.getPhone()));
    }

    private String buildRemovePatternValue(String prefix){
        return prefix + "*";
    }

}
