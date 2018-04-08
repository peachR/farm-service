package com.yiyi.farm.service.invite;

import com.yiyi.farm.dao.invite.InviteInfoDao;
import com.yiyi.farm.dao.invite.InviteRelationDao;
import com.yiyi.farm.entity.invite.InviteInfoEntity;
import com.yiyi.farm.entity.invite.InviteRelationEntity;
import com.yiyi.farm.facade.invite.InviteService;
import com.yiyi.farm.tool.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        insertRelation();
    }

    /**
     * 根据phone查子节点
     */
    public Queue<InviteRelationEntity> findChildByPhone(String phone, int[] first){
        //验证先省略

        Queue<InviteRelationEntity> result = new ConcurrentLinkedQueue<>();
        Queue<String> id = new ConcurrentLinkedQueue<>();
        id.offer(phone);
        first[0] = findAndAdd(result, id, true);
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
            int first = findDirectSuccessor(result, phones);
            while(!phones.isEmpty()){
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
        return infoDao.findRoots();
    }

    /**
     * 获取所有节点的子节点
     * @param parents
     * @return map 键为父节点值为子节点
     */
    private Map<InviteInfoEntity, List<InviteInfoEntity>> findChild(List<InviteInfoEntity> parents) {
        Map<InviteInfoEntity, List<InviteInfoEntity>> relationMap = new ConcurrentHashMap<>();
        parents.parallelStream().forEach(info -> relationMap.put(info, infoDao.findChilds(info.getUid())));
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
            for(InviteInfoEntity child : children){//每个invite节点有一个孩子就有一个对应的关系节点
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
                result.add(node);
                if(result.size() > 1000){
                    relationDao.insertRelation(result);
                    result = new ArrayList<>();
                }
                nextNodes.add(child);//记录孩子节点集合作为下一次迭代的父节点
            }
        }
        nowNodes = nextNodes;
        return result;
    }
}
