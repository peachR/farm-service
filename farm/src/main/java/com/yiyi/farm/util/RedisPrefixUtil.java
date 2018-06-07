package com.yiyi.farm.util;

/**
 * @author peach
 * @date 2018-04-25 15:12:15
 * @description redis储存时的前缀
 */
public final class RedisPrefixUtil {
    private RedisPrefixUtil(){}

    public static final String ACCESS_AUTH_PREFIX = "AUTH:";

    public static final String TABLE_CONSUME_PREFIX = "consume:";

    public static final String TABLE_INVITE_INFO_PREFIX = "info:";

    public static final String TABLE_INVITE_RELATIONTREE_PREFIX = "inviteRelation:";

    public static final String SESSIONIDNAME_PREFIX = "sessionId:";

    public static final String ACCESSAUTH_PREFIX = "accessAuth:";
}
