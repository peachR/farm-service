package com.yiyi.farm.util.generator;

import com.yiyi.farm.util.RedisPrefixUtil;

public class InfoGenerator extends KeyGenerator {
    private InfoGenerator(){}

    private final String PREFIX = RedisPrefixUtil.TABLE_INVITE_INFO_PREFIX;

    @Override
    public String getPrefix(){
        return PREFIX;
    }

    public static InfoGenerator getInstance(){
        return Singleton.generator;
    }

    private static class Singleton{
        private final static InfoGenerator generator = new InfoGenerator();
    }
}
