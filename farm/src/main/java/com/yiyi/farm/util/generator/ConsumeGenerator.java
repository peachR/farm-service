package com.yiyi.farm.util.generator;

import com.yiyi.farm.util.RedisPrefixUtil;

public class ConsumeGenerator extends KeyGenerator {
    private ConsumeGenerator(){}

    private final String PREFIX = RedisPrefixUtil.TABLE_CONSUME_PREFIX;

    @Override
    public String getPrefix(){
        return PREFIX;
    }

    public static ConsumeGenerator getInstance(){
        return Singleton.generator;
    }

    private static class Singleton{
        private final static ConsumeGenerator generator = new ConsumeGenerator();
    }
}
