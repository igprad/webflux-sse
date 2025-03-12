/* igprad - (C) 2025 */
package org.example.scheduler;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.ChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackgroundWorker {

    private final ChatService chatService;
    private final Scheduler autoScheduler;

    @Bean
    public Disposable autoArchiveChats() {
        return Flux.interval(Duration.of(30, ChronoUnit.SECONDS), autoScheduler)
                .flatMap(ignored -> {
                    // todo - lower this log level later
                    log.info("Auto archive chat invoked.");
                    return chatService.archiveChat(Instant.now().toEpochMilli());
                })
                .subscribe();
    }
}
