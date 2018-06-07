package com.yiyi.farm.util.generator;

import com.yiyi.farm.util.RedisPrefixUtil;

public class SessionGenerator extends KeyGenerator {
    private SessionGenerator(){}

    private final String prefix = RedisPrefixUtil.SESSIONIDNAME_PREFIX;

    @Override
    public String getPrefix() {
        return prefix;
    }

    private static class Singleton{
        private final static SessionGenerator generator = new SessionGenerator();
    }

    public static KeyGenerator getInstance(){
        return Singleton.generator;
    }
}
