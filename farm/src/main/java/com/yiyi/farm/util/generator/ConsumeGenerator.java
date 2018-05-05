package com.yiyi.farm.util.generator;

public class ConsumeGenerator implements KeyGenerator {
    private ConsumeGenerator(){}

    private final String PREFIX = "consume:";

    @Override
    public String getKey(String phone) {
        return PREFIX + phone;
    }

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
