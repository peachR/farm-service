package com.yiyi.farm.enumeration.good;

import com.yiyi.farm.enumeration.BaseEnum;

public enum GoodEnum implements BaseEnum {
    Organic_manure("10000001", "有机肥"),
    Chicken_feed("20000001", "鸡饲料"),
    Weed("10000003", "人工除草"),
    Dung("20000003", "人工除粪"),
    Rice("10000005", "生态米"),
    Egg("20000005", "鸡蛋"),
    Chicken_30("20000006", "鸡肉3.0斤"),
    Chicken_31("20000007", "鸡肉3.1斤"),
    Chicken_32("20000008", "鸡肉3.2斤"),
    Chicken_33("20000009", "鸡肉3.3斤"),
    Chicken_34("20000010", "鸡肉3.4斤"),
    Chicken_35("20000011", "鸡肉3.5斤"),
    Chicken_36("20000012", "鸡肉3.6斤")
    ;


    private String code;
    private String message;

    GoodEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }
}
