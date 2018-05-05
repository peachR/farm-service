package com.yiyi.farm.util.generator;

import javax.management.relation.Relation;

public class RelationGenerator implements KeyGenerator {

    private RelationGenerator(){}

    private final String PREFIX = "inviteRelation:";

    @Override
    public String getKey(String phone) {
        return PREFIX + phone;
    }

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
