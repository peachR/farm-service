package com.yiyi.farm.util.generator;

/**
 * Created by Administrator on 2018-6-27.
 */
public class RechargeGenerator implements KeyGenerator {
    private RechargeGenerator(){}

    private final String PREFIX = "recharge:";

    @Override
    public String getKey(String phone) {
        return PREFIX + phone;
    }

    @Override
    public String getPrefix(){
        return PREFIX;
    }

    public static RechargeGenerator getInstance(){
        return Singleton.generator;
    }

    private static class Singleton {
        private final static RechargeGenerator generator = new RechargeGenerator();
    }
}
