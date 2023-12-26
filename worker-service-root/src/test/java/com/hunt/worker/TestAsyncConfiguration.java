package com.hunt.worker-service-root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class TestAsyncConfiguration {

    @Bean
    @Primary
    public TaskExecutor executor() {
        return new SyncTaskExecutor();
    }
}
