package com.yiyi.farm.util.generator;

import com.yiyi.farm.util.RedisPrefixUtil;

import javax.management.relation.Relation;

public class RelationGenerator extends KeyGenerator {

    private RelationGenerator(){}

    private final String PREFIX = RedisPrefixUtil.TABLE_INVITE_RELATIONTREE_PREFIX;


    @Override
    public String getPrefix(){
        return PREFIX;
    }

    public static RelationGenerator getInstance(){
        return Singleton.generator;
    }

    private static class Singleton{
        private static final RelationGenerator generator = new RelationGenerator();
    }
}
