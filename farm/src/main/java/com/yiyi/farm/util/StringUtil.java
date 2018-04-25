package com.yiyi.farm.util;

import java.util.regex.Pattern;

/**
 * @author peach
 * @date 2018-04-18 11:13:55
 * @descripted 字符串工具类
 */
public final class StringUtil {
    //工具类,防止实例化
    private StringUtil(){}

    private static final String BACK_SLASH = "/";

    /**
     * 字符串是否是null，空字符串或者只有空格
     * @param str
     * @return
     */
    public static boolean isSpace(String str){
        if(str == null)
            return true;
        return "".equals(str.trim());
    }

    /**
     * 根据分隔符划分字符串
     * @param target 待划分字符串
     * @param dot 分隔符
     * @return 如果target为null，空字符串或者只有空格，或者dot为null返回null，否则返回划分数组
     */
    public static String[] split(String target, String dot){
        if(isSpace(target) || dot == null)
            return null;
        return target.trim().split(dot);
    }

    public static String trimUrl(String url){
        if(url.startsWith("/")){
            url = url.substring(1, url.length());
        }

        if(url.endsWith("/")){
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    public static String handleUrl(String url){
        url = trimUrl(url);
        url = replaceTo(url, "\\{.*\\}", "*");
        return url;
    }

    private static String replaceTo(String who, String from, String to){
        who = who.replaceAll(from, to);
        return who;
    }
}
