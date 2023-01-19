package com.dtors.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync  //informed spring framework to run this task in background using thread pool concept
public class AsyncConfig {

    @Bean(name = "taskExecuter")
    public Executor taskExecuter(){
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(2);
        threadPoolExecutor.setMaxPoolSize(2);
        threadPoolExecutor.setQueueCapacity(100);
        threadPoolExecutor.setThreadNamePrefix("userThread-");
        threadPoolExecutor.initialize();
        return  threadPoolExecutor;
    }

}
