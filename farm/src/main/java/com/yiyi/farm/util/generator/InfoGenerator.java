package com.yiyi.farm.util.generator;

public class InfoGenerator implements KeyGenerator {
    private InfoGenerator(){}

    private final String PREFIX = "info:";

    @Override
    public String getKey(String phone) {
        return PREFIX + phone;
    }

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
