package com.fcang.spider.hotel.provider.job;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration  
@EnableAsync  
public class ExecutorConfig {  
  
    /** Set the ThreadPoolExecutor's core pool size. */  
    private int corePoolSize = 8;  
    /** Set the ThreadPoolExecutor's maximum pool size. */  
    private int maxPoolSize = 16;  
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */  
    private int queueCapacity = 1000;  
  
    @Bean  
    public Executor providerSimpleAsync() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(corePoolSize);  
        executor.setMaxPoolSize(maxPoolSize);  
        executor.setQueueCapacity(queueCapacity);  
        executor.setThreadNamePrefix("SpiderHotelSimpleExecutor-");  
        executor.initialize();  
        return executor;  
    }  
      
}  
