package com.yiyi.farm.service.invite;

import com.yiyi.farm.dao.invite.InviteInfoDao;
import com.yiyi.farm.dao.invite.InviteRelationDao;
import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.facade.invite.InviteService;
import com.yiyi.farm.tool.Pair;
import com.yiyi.farm.tool.PosterityStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class InviteServiceImpl implements InviteService {
    @Autowired
    private InviteInfoDao infoDao;

    @Autowired
    private InviteRelationDao relationDao;

    private List<InviteInfoEntity> nowNodes;

    /**
     * 线程池，用于多线程查询子孙节点
     */
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(100,Integer.MAX_VALUE,45, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

    /**
     * 初始化关系树
     */
    public void init(){
        clearRelation();
        insertRelation();
        recordTime();
    }

    private void recordTime() {
        relationDao.recordRefreshTime();
    }

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
                    List<InviteRelationEntity> now = relationDao.findChildByPhone(phone);
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

    @Override
    public Map<String, String> findRedEnvelopeCalc(String phone, int startTime, int endTime, int totalConsume, int chargeConsume) {
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
                tempChild.addAll(relationDao.findChildByPhone(phones.poll()));
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
                if(relationDao.isNewCustomer(s,startTime,endTime)>0){
                    newCustomer++;
                    //新增的有效用户
                    if(checkValid(s,totalConsume,chargeConsume)){
                        newValidCustomer++;
                    }
                }
                //本期用户充值总额
                newTotalCharge += relationDao.findTotalCharge(s,startTime,endTime);
                //本期用户消费总额
                Map<String,BigDecimal> consume = relationDao.findConsume(s,startTime,endTime);
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
        List<InviteRelationEntity> first = relationDao.findChildByPhone(phones.poll());
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
    private Map<InviteInfoEntity, List<InviteInfoEntity>> findChild(List<InviteInfoEntity> parents) {
        Map<InviteInfoEntity, List<InviteInfoEntity>> relationMap = new ConcurrentHashMap<>();
        parents.parallelStream().forEach(info -> {
            List<InviteInfoEntity> childs = infoDao.findChilds(info.getUid());
            childs.parallelStream().forEach(child -> child.setHigh(info.getHigh() + 1));
            relationMap.put(info, childs);
        });
        return relationMap;
    }

    /**
     * 插入所有节点
     *
     */
    private void insertRelation() {
        nowNodes = findRoots();

        while (nowNodes.size() != 0) {
            List<InviteRelationEntity> list = buildRelation(nowNodes);
            System.out.println(list.size() + " next: "+ nowNodes.size());
            System.out.println();
            relationDao.insertRelation(list);
        }
    }

    /**
     * 构建关系节点
     * @param list
     * @return
     */
    private List<InviteRelationEntity> buildRelation(List<InviteInfoEntity> list){
        List<InviteRelationEntity> result = new ArrayList<>(list.size());//本层关系节点
        Map<InviteInfoEntity, List<InviteInfoEntity>> map = findChild(list);
        List<InviteInfoEntity> nextNodes = new ArrayList<>();
        Set<Map.Entry<InviteInfoEntity, List<InviteInfoEntity>>> sets = map.entrySet();
//        sets.parallelStream().forEach(entry -> {
////
////        });
        for(Map.Entry<InviteInfoEntity, List<InviteInfoEntity>> entry: sets){
            InviteInfoEntity parent = entry.getKey();
            List<InviteInfoEntity> children = entry.getValue();
            if(children == null || children.size() == 0){
                buildRelationAndAdd(result, parent, null);
            }else {
                for (InviteInfoEntity child : children) {//每个invite节点有一个孩子就有一个对应的关系节点
                    buildRelationAndAdd(result, parent, child);
                    if (result.size() > 1000) {
                        relationDao.insertRelation(result);
                        result = new ArrayList<>();
                    }
                    nextNodes.add(child);//记录孩子节点集合作为下一次迭代的父节点
                }
            }
        }
        nowNodes = nextNodes;
        return result;
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



}
