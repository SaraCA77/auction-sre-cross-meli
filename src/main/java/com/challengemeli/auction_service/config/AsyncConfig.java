package com.challengemeli.auction_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);       // núcleos iniciales
        executor.setMaxPoolSize(50);        // máximo threads
        executor.setQueueCapacity(100);     // cola antes de crear threads extra
        executor.setThreadNamePrefix("AuctionAsync-");
        executor.initialize();
        return executor;
    }
}
