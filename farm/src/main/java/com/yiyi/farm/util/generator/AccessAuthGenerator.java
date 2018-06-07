package com.yiyi.farm.util.generator;

import com.yiyi.farm.util.RedisPrefixUtil;


public class AccessAuthGenerator extends KeyGenerator {
    private AccessAuthGenerator(){}

    private final String PREFIX = RedisPrefixUtil.ACCESSAUTH_PREFIX;


    @Override
    public String getPrefix() {
        return PREFIX;
    }

    public static KeyGenerator getInstance(){
        return Singleton.generator;
    }

    private static class Singleton{
        private static final AccessAuthGenerator generator = new AccessAuthGenerator();
    }
}
