/* igprad - (C) 2025 */
package org.example.configuration;

import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SchedulerConfiguration {

    @Bean
    public Scheduler commonScheduler() {
        return Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor(), "common-executor");
    }

    @Bean
    public Scheduler repositoryScheduler() {
        return Schedulers.fromExecutorService(
                Executors.newScheduledThreadPool(10, Executors.defaultThreadFactory()), "repository-executor");
    }

    @Bean
    public Scheduler autoScheduler() {
        return Schedulers.fromExecutorService(Executors.newSingleThreadScheduledExecutor());
    }
}
