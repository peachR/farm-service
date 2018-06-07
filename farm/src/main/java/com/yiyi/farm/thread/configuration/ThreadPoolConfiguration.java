package com.yiyi.farm.thread.configuration;

import com.yiyi.farm.thread.EventProcessThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfiguration {
    @Bean
    public ThreadPoolExecutor initEventProcessThreadPool(){
        return new EventProcessThreadPool(1, 1, 0);
    }
}
