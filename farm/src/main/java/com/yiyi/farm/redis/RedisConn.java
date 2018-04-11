package com.yiyi.farm.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author peach
 * @date 2018-04-10 15:21:01
 * @description Redis连接信息配置类
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConn {
    private String host;
    private int port;
    private int timeout;
    private int database;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "RedisConn{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", timeout=" + timeout +
                ", database=" + database +
                '}';
    }
}
