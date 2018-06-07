package com.yiyi.farm.service.invite;

import com.yiyi.farm.dao.invite.ConsumeLogDao;
import com.yiyi.farm.dao.invite.InviteInfoDao;
import com.yiyi.farm.dao.invite.InviteRelationDao;
import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.facade.invite.InviteService;
import com.yiyi.farm.req.invite.InviteReq;
import com.yiyi.farm.tool.Pair;
import com.yiyi.farm.tool.PosterityStatistics;
import com.yiyi.farm.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class InviteServiceImpl implements InviteService {
    @Autowired
    private InviteInfoDao infoDao;

    @Autowired
    private InviteRelationDao relationDao;

    @Autowired
    private InviteCache inviteCache;

    @Autowired
    private ConsumeLogDao consumeLogDao;

    private List<InviteInfoEntity> nowNodes;

    private Map<String, List<InviteInfoEntity>> cacheMap;


    /**
     * 线程池，用于多线程查询子孙节点
     */
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 400,45, TimeUnit.SECONDS,new ArrayBlockingQueue<>(819200));

    /**
     * 缓存初始化(缓存预热)
     * @return
     */
    @Override
    public boolean initCaching(){
        inviteCache.clearCache();
        inviteCache.cacheInviteInfo();
        inviteCache.cacheLogConsume();
        inviteCache.cahceInviteRelation();
        return true;
    }


    /**
     * 初始化关系树
     */
    public void init(){
        long start = System.nanoTime();
        clearRelation();
        insertRelation();
        recordTime();
        clearCache();
        System.out.println("init over: " + ((System.nanoTime() - start) / 1_000_000));
    }


    /**
     * 记录重建时间
     */
    private void recordTime() {
        relationDao.recordRefreshTime();
    }

    /**
     * 清空关系表
     */
    private void clearRelation() {
        relationDao.clearRelation();
    }

    /**
     * 根据phone查子节点
     */
    public Queue<InviteRelationEntity> findChildByPhone(String phone, int[] first){
        //验证先省略

        Queue<InviteRelationEntity> result = new ConcurrentLinkedQueue<>();
        Queue<String> id = new ConcurrentLinkedQueue<>();
        id.offer(phone);
        first[0] = findAndAdd(result, id, true);//获取直接孩子的个数
        return result;
    }

    /**
     * 根据uid查询子孙节点
     * @param uid
     * @return
     */
    public Queue<InviteRelationEntity> findChildByUid(String uid, int[] first){
        //验证先省略

        Queue<InviteRelationEntity> result = new ConcurrentLinkedQueue<>();
        Queue<String> id = new ConcurrentLinkedQueue<>();
        id.offer(uid);
        System.out.println("find");
        first[0] = findAndAdd(result, id, false);
        return result;
    }

    @Override
    public Queue<InviteRelationEntity> findChildByPhone(String phone) {
        return findChildByPhone(phone, new int[1]);
    }

    @Override
    public Queue<InviteRelationEntity> findChildByUid(String uid) {
        return findChildByUid(uid, new int[1]);
    }

    @Override
    public Pair findChildNumbersByPhone(String phone) {
        int[] first = new int[1];
        int all = findChildByPhone(phone,first).size();
        return new Pair(first[0], all);
    }

    /**
     * 迭代查询所有子孙节点并添加到结果集
     * @param result
     * @param phones
     * @param isPhone
     */
    private int findAndAdd(Queue<InviteRelationEntity> result, Queue<String> phones, boolean isPhone) {
        final Executor executor = this.executor;//获取线程池
        if(isPhone) {
            int first = findDirectSuccessor(result, phones);//将直接孩子加入结果队列，同时将孩子作为下一级的父节点加入phones队列，返回直接孩子的个数
            while(!phones.isEmpty()){//多线程迭代查询
                final String phone = phones.poll();
                executor.execute(()->{
                    List<InviteRelationEntity> now = inviteCache.findChildByPhone(phone);
                    if(now.size() != 0){
                        result.addAll(now);
                        for(InviteRelationEntity entity : now){
                            phones.offer(entity.getChildPlayerPhone());
                        }
                    }
                });
            }
            return first;
        }else{
            while(!phones.isEmpty()){
                String phone = phones.poll();
                List<InviteRelationEntity> now = relationDao.findChildByUid(phone);
                if(now.size() != 0){
                    result.addAll(now);
                    for(InviteRelationEntity entity : now){
                        phones.offer(entity.getChildPlayerUid());
                    }
                }
            }
            return 0;//暂时
        }
    }

    public List<PosterityStatistics> findRelationNumberMap(String phone,int totalConsume,int chargeConsume){
        int high = 0;
        Map<Integer,PosterityStatistics> map = new LinkedHashMap<>();
        Queue<String> phones = new LinkedList<>();
        phones.offer(phone);
        while (phones.size()!=0){
            List<InviteRelationEntity> tempChild = new ArrayList<>();
            while (phones.size()!=0){
                tempChild.addAll(relationDao.findChildByPhone(phones.poll()));
            }

            phones.clear();
            for(InviteRelationEntity entity : tempChild){
                if(entity.getChildPlayerPhone()!=null){
                    phones.offer(entity.getChildPlayerPhone());
                }
            }
            int total = 0;
            int valid = 0;
            for(String s : phones){
                total++;
                if(checkValid(s,totalConsume,chargeConsume)){
                    valid++;
                }
            }
            map.put(++high,new PosterityStatistics(total,valid));
        }
        int total0 = 0;
        int valid0 = 0;
        for(PosterityStatistics node : map.values()){
            total0 += node.getTotal();
            valid0 += node.getValid();
        }
        map.put(0,new PosterityStatistics(total0,valid0));
        List<PosterityStatistics> result = new ArrayList<>();
        result.add(map.get(0));
        for(int i = 1; i < map.size()-1;i++){
            result.add(map.get(i));
        }
        return result;
    }

    /**
     * findRedEnvelopeCalc并发版本
     * @param phone
     * @param inviteReq
     * @return
     */
    public Map<String, String> findRedEnvelopeCalcParallel(final String phone, InviteReq inviteReq){
        ChildCustomer childCustomer = new ChildCustomer();//统计子节点用户信息
        QueryCondition queryCondition = new QueryCondition(inviteReq);

        long start, times = 1l;

        start = System.nanoTime();
        Queue<String> phones = findFirstLevelChild(phone, childCustomer, queryCondition);
        System.out.println("findFirstLevelChild: " + (System.nanoTime() - start) / 1_000_000);
        while(phones.size() != 0){
            start = System.nanoTime();

            List<CompletableFuture<List<String>>> relationsFutures = phones.stream()
                    .map(p -> CompletableFuture.supplyAsync(() -> relationDao.findChildByPhone(p), executor))//转换为异步任务
                    .map(future -> future.thenApply(relationList -> relationList.stream()
                            .map(relation -> relation.getChildPlayerPhone()).collect(Collectors.toList())))//查询完成后转换为孩子号码列表
                    .collect(Collectors.toList());
            Queue<String> tempPhones = new ConcurrentLinkedQueue<>();
            //等待所有异步任务完成后添加号码
            relationsFutures.stream().map(CompletableFuture::join).forEach(list -> {
                for(String str: list){
                    if(!StringUtil.isSpace(str))
                        tempPhones.add(str);
                }
            });

            System.out.println("getChildPhone " + times + ": " + (System.nanoTime() - start) / 1_000_000);

            start = System.nanoTime();

            parallelStatisticalResult(childCustomer, queryCondition, tempPhones);
            System.out.println("statistical " + times++ + ": " + (System.nanoTime() - start) / 1_000_000);

            phones = tempPhones;
        }

       return buildChildCustomerResultMap(phone, childCustomer);
    }

    private void parallelStatisticalResult(ChildCustomer childCustomer, QueryCondition queryCondition, Queue<String> phones) {
        List<CompletableFuture<Integer>> statistical = phones.stream().map(phone -> CompletableFuture.supplyAsync(() -> {
            statisticalResult(queryCondition, childCustomer, phone);
            return 0;//适配器
        },executor)).collect(Collectors.toList());
        statistical.stream().map(CompletableFuture::join).forEach(i -> {return;});
    }

    /**
     * 单线程查询第一层节点孩子
     * @param phone
     * @param childCustomer
     * @param queryCondition
     * @return
     */
    private Queue<String> findFirstLevelChild(String phone, ChildCustomer childCustomer, QueryCondition queryCondition) {
        Queue<String> phones = new ConcurrentLinkedQueue<>();
        phones.offer(phone);

        if(phones.size() != 0){//第一层寻找，单线程
            List<InviteRelationEntity> tempChild = new ArrayList<>();
            tempChild.addAll(inviteCache.findChildByPhone(phones.poll()));
            for(InviteRelationEntity entity : tempChild){//添加第一层子节点
                if(entity.getChildPlayerPhone()!=null){
                    phones.offer(entity.getChildPlayerPhone());
                }
            }

            parallelStatisticalResult(childCustomer, queryCondition, phones);
        }
        return phones;
    }

    /**
     * 构建子节点查询结果映射
     * @param phone
     * @param childCustomer
     * @return
     */
    private Map<String,String> buildChildCustomerResultMap(String phone, ChildCustomer childCustomer){
        Map<String,String> result = new HashMap<>();
        result.put("phone",phone);
        result.put("newCustomer",childCustomer.getNewCustomer().toString());
        result.put("newValidCustomer", childCustomer.getNewValidCustomer().toString());
        result.put("newTotalCharge", childCustomer.getNewTotalCharge().toString());
        result.put("newTotalConsume", childCustomer.getNewTotalConsume().toString());
        result.put("newTotalConsumeFromCharge", childCustomer.getNewTotalConsumeFromCharge().toString());
        return result;
    }

    /**
     * 统计结果信息
     * @param queryCondition 查询条件封装类
     * @param childCustomer
     * @param phone
     */
    private void statisticalResult(QueryCondition queryCondition, ChildCustomer childCustomer, String phone) {
        long start;

        start = System.nanoTime();

        queryCondition.statisticsNewCustomer(childCustomer.getNewCustomer(), childCustomer.getNewValidCustomer(), phone);

        System.out.println("inner getNewValidCustomer: " + (System.nanoTime() - start) / 1_000_000);
        //本期用户充值总额
        start = System.nanoTime();
        childCustomer.addAndGet(childCustomer.getNewTotalCharge(), relationDao.findTotalCharge(phone,queryCondition.getStartTime(),queryCondition.getEndTime()));
        System.out.println("inner findTotalCharge: " + (System.nanoTime() - start) / 1_000_000);
        //本期用户消费总额
        start = System.nanoTime();
        Map<String,Integer> consume = inviteCache.findConsume(phone,queryCondition.getStartTime(),queryCondition.getEndTime());
        childCustomer.addAndGet(childCustomer.getNewTotalConsume(),consume.get("total"));
        System.out.println("inner findConsume: " + (System.nanoTime() - start) / 1_000_000);

        //本期用户消费的充值金额
        childCustomer.addAndGet(childCustomer.getNewTotalConsumeFromCharge(),consume.get("charge"));
    }



    @Override
    public Map<String, String> findRedEnvelopeCalc(String phone, InviteReq inviteReq) {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println();
//            }
//        }, 0, 1000);
        Map<String,String> result = new HashMap<>();
        Integer newCustomer = 0;
        Integer newValidCustomer = 0;
        Integer newTotalCharge = 0;
        Integer newTotalConsume = 0;
        Integer newTotalConsumeFromCharge = 0;

        Queue<String> phones = new LinkedList<>();
        phones.offer(phone);
        System.out.println(phones.size());
        while (phones.size()!=0){
            List<InviteRelationEntity> tempChild = new ArrayList<>();
            while (phones.size()!=0){
                tempChild.addAll(inviteCache.findChildByPhone(phones.poll()));
            }

            phones.clear();
            for(InviteRelationEntity entity : tempChild){
                if(entity.getChildPlayerPhone()!=null){
                    phones.offer(entity.getChildPlayerPhone());
                }
            }
            System.out.println(phones.size());
            for(String s : phones){
                //新增用户
                if(relationDao.isNewCustomer(s,inviteReq.getStartTime(),inviteReq.getEndTime())>0){
                    newCustomer++;
                    //新增的有效用户
                    if(checkValid(s,inviteReq.getTotalConsume(),inviteReq.getChargeConsume())){
                        newValidCustomer++;
                    }
                }
                //本期用户充值总额
                newTotalCharge += relationDao.findTotalCharge(s, inviteReq.getStartTime(),inviteReq.getEndTime());
                //本期用户消费总额
                Map<String,BigDecimal> consume = consumeLogDao.findConsume(s,inviteReq.getStartTime(),inviteReq.getEndTime());
                newTotalConsume += consume.get("total").intValue();
                //本期用户消费的充值金额
                newTotalConsumeFromCharge += consume.get("charge").intValue();
            }
        }
        result.put("phone",phone);
        result.put("newCustomer",newCustomer.toString());
        result.put("newValidCustomer",newValidCustomer.toString());
        result.put("newTotalCharge",newTotalCharge.toString());
        result.put("newTotalConsume",newTotalConsume.toString());
        result.put("newTotalConsumeFromCharge",newTotalConsumeFromCharge.toString());
        return result;
    }

    @Override
    public List<String> findRefreshTime() {
        return relationDao.findRefreshTime();
    }

    private boolean checkValid(String phone,int totalConsume,int chargeConsume){
        Map<String,BigDecimal> result = relationDao.checkValidCustomer(phone);
        if(result.get("total").intValue() >= totalConsume && result.get("charge").intValue() >= chargeConsume){
            return true;
        }
        return false;
    }
    /**
     * 寻找直接孩子
     * @param result
     * @param phones
     * @return 直接孩子个数
     */
    private int findDirectSuccessor(Queue<InviteRelationEntity> result, Queue<String> phones) {
        List<InviteRelationEntity> first = inviteCache.findChildByPhone(phones.poll());
        if(first.size() != 0){
            result.addAll(first);
            for(InviteRelationEntity entity : first){
                phones.offer(entity.getChildPlayerPhone());
            }
        }
        System.out.println(phones.size());
        return first.size();
    }

    /**
     * 获取所有根节点
     * @return
     */
    private List<InviteInfoEntity> findRoots() {
        List<InviteInfoEntity> roots = infoDao.findRoots();
        roots.parallelStream().forEach(root -> root.setHigh(1));
        return roots;
    }

    /**
     * 获取所有节点的子节点
     * @param parents
     * @return map 键为父节点值为子节点
     */
    private Map<InviteInfoEntity, List<InviteInfoEntity>> findChildAndBuildRelationMap(List<InviteInfoEntity> parents) {
        //记录父节点与子节点对应关系，一个父节点可以有多个子节点
        Map<InviteInfoEntity, List<InviteInfoEntity>> relationMap = new ConcurrentHashMap<>();
        cacheInviteInfoByUpPalyerPhone(infoDao.findAll());

        parents.parallelStream().forEach(parent -> {
            List<InviteInfoEntity> childs = cacheMap.get(parent.getPhone());
            setChildNodeHigh(parent, childs);
            relationMap.put(parent, childs);
        });

//        parents.parallelStream().forEach(parent -> {
//            List<InviteInfoEntity> childs = infoDao.findChilds(parent.getUid());
//            setChildNodeHigh(parent, childs);
//            relationMap.put(parent, childs);
//        });
//        List<CompletableFuture<List<InviteInfoEntity>>> tasks = parents.stream().map(parent -> CompletableFuture.supplyAsync(() -> {
//            List<InviteInfoEntity> childs = infoDao.findChilds(parent.getUid());
//            setChildNodeHigh(parent, childs);
//            relationMap.put(parent, childs);
//            return childs;
//        }, executor)).collect(Collectors.toList());
//        tasks.stream().map(CompletableFuture::join).count();
        return relationMap;
    }

    private void cacheInviteInfoByUpPalyerPhone(List<InviteInfoEntity> all) {
        cacheMap = all.stream().collect(Collectors.groupingBy(info -> info.getUpPlayerPhone()));
    }

    //为子节点设定高度，高度为父节点+1
    private void setChildNodeHigh(InviteInfoEntity parent, List<InviteInfoEntity> childs) {
        childs.parallelStream().forEach(child -> child.setHigh(parent.getHigh() + 1));
    }

    private void clearCache(){
        cacheMap = null;
    }

    /**
     * 插入所有节点
     *
     */
    private void insertRelation() {
        nowNodes = findRoots();

        while (nowNodes.size() != 0) {
            List<InviteRelationEntity> list = buildRelation(nowNodes);
            relationDao.insertRelation(list);
        }
    }

    /**
     * 构建关系节点
     * @param list
     * @return
     */
    private List<InviteRelationEntity> buildRelation(List<InviteInfoEntity> list){
        List<InviteRelationEntity> relationEntityList = new ArrayList<>(list.size());//本层关系节点
        Map<InviteInfoEntity, List<InviteInfoEntity>> relationMap = findChildAndBuildRelationMap(list);
        List<InviteInfoEntity> nextNodes = new ArrayList<>();//下一层孩子集合
        Set<Map.Entry<InviteInfoEntity, List<InviteInfoEntity>>> sets = relationMap.entrySet();
        for(Map.Entry<InviteInfoEntity, List<InviteInfoEntity>> entry: sets){
            InviteInfoEntity parent = entry.getKey();
            List<InviteInfoEntity> children = entry.getValue();
            if(children == null || children.size() == 0){
                buildRelationAndAdd(relationEntityList, parent, null);
            }else {
                for (InviteInfoEntity child : children) {//每个invite节点有一个孩子就有一个对应的关系节点
                    buildRelationAndAdd(relationEntityList, parent, child);
                    if (relationEntityList.size() > 1000) {
                        relationDao.insertRelation(relationEntityList);
                        //relationEntityList = new ArrayList<>();
                        relationEntityList.clear();
                    }
                    nextNodes.add(child);//记录孩子节点集合作为下一次迭代的父节点
                }
            }
        }
        nowNodes = nextNodes;
        return relationEntityList;
    }

    /**
     * 创建关系节点并加入带插入队列
     * @param result
     * @param parent
     * @param child
     */
    private void buildRelationAndAdd(List<InviteRelationEntity> result, InviteInfoEntity parent, InviteInfoEntity child) {
        if(child == null)
            child = new InviteInfoEntity();
        InviteRelationEntity node = new InviteRelationEntity();
        node.setChildPlayerUid(child.getUid());
        node.setChildPlayerPhone(child.getPhone());
        node.setUpPlayerPhone(parent.getUpPlayerPhone());
        node.setUpPlayerUid(parent.getUpPlayerUid());
        node.setServerId(parent.getServerId());
        node.setPlayerId(parent.getPlayerId());
        node.setUid(parent.getUid());
        node.setPhone(parent.getPhone());
        node.setTime(child.getTime());
        node.setHigh(parent.getHigh());
        result.add(node);
    }

    /**
     * 查询条件
     */
    private class QueryCondition{
        final int startTime;
        final int endTime;
        final int totalConsume;
        final int chargeConsume;

        public QueryCondition(int startTime, int endTime, int totalConsume, int chargeConsume) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.totalConsume = totalConsume;
            this.chargeConsume = chargeConsume;
        }

        public QueryCondition(InviteReq inviteReq){
            this.startTime = inviteReq.getStartTime();
            this.endTime = inviteReq.getEndTime();
            this.totalConsume = inviteReq.getTotalConsume();
            this.chargeConsume = inviteReq.getChargeConsume();
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public int getTotalConsume() {
            return totalConsume;
        }

        public int getChargeConsume() {
            return chargeConsume;
        }

        /**
         * 统计新用户
         * @param newCustomer
         * @param newValidCustomer
         * @param phone
         */
        public void statisticsNewCustomer(AtomicInteger newCustomer, AtomicInteger newValidCustomer, String phone) {
//            if(relationDao.isNewCustomer(s,startTime,endTime)>0){
            if(inviteCache.isNewCustomer(phone, startTime, endTime)){
                newCustomer.incrementAndGet();
                //新增的有效用户
                if(checkValid(phone,totalConsume,chargeConsume)){
                    newValidCustomer.incrementAndGet();
                }
            }
        }
    }

    /**
     * 某节点下的孩子用户
     */
    private class ChildCustomer{
        AtomicInteger newCustomer;//新增用户
        AtomicInteger newValidCustomer;//新增有效用户
        AtomicInteger newTotalCharge;//总充值金额，所有孩子并非新增用户的
        AtomicInteger newTotalConsume;//总消费金额
        AtomicInteger newTotalConsumeFromCharge;//总消费金额中的充值部分

        ChildCustomer(){
            newCustomer = new AtomicInteger(0);
            newValidCustomer = new AtomicInteger(0);
            newTotalCharge = new AtomicInteger(0);
            newTotalConsume = new AtomicInteger(0);
            newTotalConsumeFromCharge = new AtomicInteger(0);
        }

        public AtomicInteger getNewCustomer() {
            return newCustomer;
        }

        public AtomicInteger getNewValidCustomer() {
            return newValidCustomer;
        }

        public AtomicInteger getNewTotalCharge() {
            return newTotalCharge;
        }

        public AtomicInteger getNewTotalConsume() {
            return newTotalConsume;
        }

        public AtomicInteger getNewTotalConsumeFromCharge() {
            return newTotalConsumeFromCharge;
        }

        /**
         * CAS增加并获取结果
         * @param who 待增加的字段
         * @param value 增加值
         * @return 增加后的结果
         */
        public int addAndGet(AtomicInteger who, int value){
            return who.addAndGet(value);
        }
    }
}
