package com.yiyi.farm.service.invite;

import com.yiyi.farm.dao.invite.ConsumeLogDao;
import com.yiyi.farm.dao.invite.InviteInfoDao;
import com.yiyi.farm.dao.invite.InviteRelationDao;
import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.entity.invite.LogConsumeEntity;
import com.yiyi.farm.req.invite.InviteReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018-5-15.
 */
@Service("inviteService")
public class TempInviteServiceImpl {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ConsumeLogDao consumeLogDao;
    @Autowired
    private InviteInfoDao inviteInfoDao;
    @Autowired
    private InviteRelationDao inviteRelationDao;

    public static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 获取邀请数Rank
     * @return
     */
    public List<Map<String,String>> getRank(InviteReq inviteReq){
        //获得所有phone
        Map<String, List<InviteRelationEntity>> map = inviteRelationDao.findAllRelation().stream().collect(Collectors.groupingBy(InviteRelationEntity::getPhone));
        List<String> phones = new ArrayList<>(map.keySet());
        System.out.println("phones get");
        //结果集
        Map<String,Integer> ranks = new TreeMap<>();

        for(String phone : phones){
            //获得这个phone的统计信息
            Map<String, ChildStatistics> childStatistics = findStatistics(phone,inviteReq);
            //移除第0层
            Set<String> levels = childStatistics.keySet();
            levels.remove("0");
            //新增用户总数量
            int childs = 0;
            for (String level : levels){
                childs += childStatistics.get(level).getNewCustomer().intValue();
            }
            ranks.put(phone,childs);
        }
        System.out.println("info get");
        List<Map.Entry<String, Integer>> entryArrayList = new ArrayList<>(ranks.entrySet());
        Collections.sort(entryArrayList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return  - o1.getValue() + o2.getValue();
            }
        });
        System.out.println("sort get");
        List<Map<String,String>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entryArrayList) {
            Map<String,String> phoneAndChilds = new HashMap<>();
            phoneAndChilds.put("phone",entry.getKey());
            phoneAndChilds.put("childs",String.valueOf(entry.getValue()));
            result.add(phoneAndChilds);
        }
        System.out.println("return get");
        return result;
    }
    public List<String> test(){
        Map<String, List<InviteRelationEntity>> map = inviteRelationDao.findAllRelation().stream().collect(Collectors.groupingBy(InviteRelationEntity::getPhone));
        List<String> phones = new ArrayList<>(map.keySet());
        long start = System.currentTimeMillis();
        InviteReq inviteReq = new InviteReq();
        inviteReq.setTotalConsume(0);
        inviteReq.setStartTime(1293814860);
        inviteReq.setEndTime(1526981099);
        phones.stream().forEach(p->findStatistics(p,inviteReq));
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        return phones;
    }
    public boolean initCache() throws ExecutionException, InterruptedException {
        flushDB();
        initConsumeLog();
        initInviteInfo();
        initInviteRelation();
        redisTemplate.opsForValue().set("cacheOrNot","yes");
        return true;
    }

    /**
     * 初始化consumeLog的cache
     */
    private boolean initConsumeLog(){
        List<LogConsumeEntity> consumeEntityList = consumeLogDao.findAll();
        Map<String,List<LogConsumeEntity>> groupByPhone = consumeEntityList.stream().collect(Collectors.groupingBy(LogConsumeEntity::getPhone));
        for(String key : groupByPhone.keySet()){
            redisTemplate.opsForList().rightPushAll("consume:"+key,groupByPhone.get(key));
        }
        return true;
    }

    /**
     * 初始化inviteInfo的cache
     */
    private boolean initInviteInfo(){
        List<InviteInfoEntity> inviteInfoEntityList = inviteInfoDao.findAll();
        Map<String,List<InviteInfoEntity>> groupByPhone = inviteInfoEntityList.stream().collect(Collectors.groupingBy(InviteInfoEntity::getPhone));
        for(String key : groupByPhone.keySet()){
            redisTemplate.opsForList().rightPushAll("info:"+key,groupByPhone.get(key));
        }
        return true;
    }

    /**
     * 初始化inviteRelation的cache
     */
    private boolean initInviteRelation(){
        List<InviteRelationEntity> inviteRelationEntityList = inviteRelationDao.findAllRelation();
        Map<String,List<InviteRelationEntity>> groupByPhone = inviteRelationEntityList.stream().collect(Collectors.groupingBy(InviteRelationEntity::getPhone));
        for(String key : groupByPhone.keySet()){
            redisTemplate.opsForList().rightPushAll("inviteRelation:"+key,groupByPhone.get(key));
        }
        return true;
    }

    /**
     * 清空redis
     */
    private void flushDB(){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.flushDb();
                return "";
            }
        });
    }

    /**
     * 从inviteRelation的cache中找出某phone的子节点
     * @param phone
     * @return
     */
    private Queue<String> findChildrenByPhone(String phone){
        Queue<String> phones = new ConcurrentLinkedQueue<>();
        List<InviteRelationEntity> results = redisTemplate.opsForList().range("inviteRelation:" + phone,0,-1);
        for(InviteRelationEntity entity : results){
            if(entity.getChildPlayerPhone() != null){
                phones.offer(entity.getChildPlayerPhone());
            }
        }
        return phones;
    }

    /**
     * 判断是否新增用户
     * @param phone
     * @param inviteReq
     * @return
     */
    private boolean isNewCustomer(String phone, InviteReq inviteReq){
        List<InviteInfoEntity> customer = redisTemplate.opsForList().range("info:"+phone,0,-1);
        for(InviteInfoEntity entity: customer){
            if(timeBetween(entity.getTime(),inviteReq.getStartTime(),inviteReq.getEndTime()))
                return true;
        }
        return false;
    }

    private static boolean timeBetween(Timestamp timestamp,int start,int end){
        Long time = timestamp.getTime();
        if(time.intValue() >= start && time.intValue() <= end){
            return true;
        }
        return false;
    }
    /**
     * 判断是否是有效用户
     * @param phone
     * @param inviteReq
     * @return
     */
    private boolean isNewValidCustomer(String phone,InviteReq inviteReq){
        if(isNewCustomer(phone,inviteReq)){
            List<LogConsumeEntity> consumes = redisTemplate.opsForList().range("consume:"+phone,0,-1);
            int consumeValue = consumes.stream()
                    .map(LogConsumeEntity::getMoney)
                    .reduce(0,Integer::sum);
            if(consumeValue >= inviteReq.getTotalConsume()){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    
    /**
     * 获取此phone在这个时间段内的消费
     * @param phone
     * @param inviteReq
     * @return
     */
    private int getNewConsumeValue(String phone,InviteReq inviteReq){
        List<LogConsumeEntity> consumes = redisTemplate.opsForList().range("consume:"+phone,0,-1);
        int consumeValue = consumes.stream()
                .filter(consume->{
                    if(timeBetween(consume.getTime(),inviteReq.getStartTime(),inviteReq.getEndTime())){
                        return true;
                    }else {
                        return false;
                    }
                })
                .map(LogConsumeEntity::getMoney)
                .reduce(0,Integer::sum);
        return consumeValue;
    }

    private void countSingleLevel(ChildStatistics statistics,String phone,InviteReq inviteReq){
        //新增有效用户数量加1
        if(isNewValidCustomer(phone,inviteReq)){
            statistics.getNewValidCustomer().incrementAndGet();
        }
        //新增消费金额
        statistics.getNewTotalConsume().addAndGet(getNewConsumeValue(phone,inviteReq));
        //总用户数
        statistics.getTotalCustomer().incrementAndGet();
        //新增用户数
        if(isNewCustomer(phone,inviteReq)){
            statistics.getNewCustomer().incrementAndGet();
        }
    }

    public Map<String, ChildStatistics> findStatistics(String phone, InviteReq inviteReq){
        if(findChildrenByPhone(phone).size() == 0){
            return null;
        }
        Queue<String> phones = new ConcurrentLinkedQueue<>();
        phones.offer(phone);
        //层数
        AtomicInteger high = new AtomicInteger(0);
        //统计结果
        Map<String,ChildStatistics> statisticsMap = new ConcurrentHashMap<>();
        while(phones.size() != 0){

            //本层的统计信息
            ChildStatistics thisLevelStatistics = new ChildStatistics();
            List<CompletableFuture<Integer>> statisticsFutures = phones.stream()
                    .map(p->CompletableFuture.supplyAsync(()->{
                        countSingleLevel(thisLevelStatistics,p,inviteReq);
                        return 0;
                    }))
                    .collect(Collectors.toList());



            List<CompletableFuture<Queue<String>>> ChildFutures = phones.stream()
                    .map(p->CompletableFuture.supplyAsync(()->findChildrenByPhone(p)))
                    .collect(Collectors.toList());

            //等待统计结束
            statisticsFutures.stream()
                    .map(CompletableFuture::join)
                    .forEach(i->{return;});

            int key = high.getAndIncrement();
            statisticsMap.put(String.valueOf(key),thisLevelStatistics);

            //下一级子节点
            Queue<String> childPhones = new ConcurrentLinkedQueue<>();
            ChildFutures.stream()
                   .map(CompletableFuture::join)
                   .forEach(phoneQueue->{
                       for(String tempPhone :phoneQueue){
                           childPhones.add(tempPhone);
                       }
                   });


            phones = childPhones;
        }
        return statisticsMap;
    }

    public class ChildStatistics implements Serializable{
        AtomicInteger newValidCustomer;//新增有效用户
        AtomicInteger newTotalConsume;//总消费金额
        AtomicInteger totalCustomer;//该层总用户数
        AtomicInteger newCustomer; //新增用户数

        ChildStatistics() {
            this.newValidCustomer = new AtomicInteger(0);
            this.newTotalConsume = new AtomicInteger(0);
            this.totalCustomer = new AtomicInteger(0);
            this.newCustomer = new AtomicInteger(0);
        }

        public AtomicInteger getNewValidCustomer() {
            return newValidCustomer;
        }

        public AtomicInteger getNewTotalConsume() {
            return newTotalConsume;
        }

        public AtomicInteger getTotalCustomer() {
            return totalCustomer;
        }

        public AtomicInteger getNewCustomer() {
            return newCustomer;
        }
    }
}
