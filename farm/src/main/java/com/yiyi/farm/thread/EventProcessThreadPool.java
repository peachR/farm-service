package com.yiyi.farm.thread;

import java.util.concurrent.*;

public class EventProcessThreadPool extends ThreadPoolExecutor {
    public EventProcessThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime){
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), Executors.defaultThreadFactory());
    }

    public EventProcessThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue<Runnable> workQueue){
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, Executors.defaultThreadFactory());
    }
}
