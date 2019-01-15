package com.fcang.spider.hotel.provider.job;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fcang.spider.hotel.provider.biz.PhantomjsLoader;

@Configuration  
@EnableAsync  
public class ExecutorConfig {  
  
    /** Set the ThreadPoolExecutor's core pool size. */  
    private int corePoolSize = 8;  
    /** Set the ThreadPoolExecutor's maximum pool size. */  
    private int maxPoolSize = 16;  
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */  
    private int queueCapacity = 1000;  
  
    @Bean("providerSimpleAsync")
    public Executor providerSimpleAsync() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(corePoolSize);  
        executor.setMaxPoolSize(maxPoolSize);  
        executor.setQueueCapacity(queueCapacity);  
        executor.setThreadNamePrefix("SpiderHotelSimpleExecutor-");  
        executor.initialize();  
        return executor;  
    }  
    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
        return executor;
    }

    
    @Bean("phantomjsAsyncExecutor")  
    public Executor phantomjsAsyncExecutor() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        if(PhantomjsLoader.isWindows) {
        	executor.setCorePoolSize(2);  
        }else {
        	executor.setCorePoolSize(3);  
        }
        executor.setMaxPoolSize(6);  
        executor.setQueueCapacity(queueCapacity);  
        executor.setThreadNamePrefix("phantomjsAsyncExecutor-");  
        executor.initialize();  
        return executor;  
    }  
      
}  
