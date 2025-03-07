/* igprad - (C) 2025 */
package org.example.service.impl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.EventService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final Scheduler commonScheduler;

    @Override
    public Flux<ServerSentEvent<String>> stringEventStream() {
        return Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
                .map(this::construct)
                .publishOn(commonScheduler)
                .subscribeOn(commonScheduler)
                .doOnEach(signal -> log.info("stream event : {}", signal.get()));
    }

    private ServerSentEvent<String> construct(Long second) {
        return ServerSentEvent.<String>builder()
                .event("webflux-sse-event-test")
                .data("Streamed event in " + second + " second.")
                .comment("test")
                .build();
    }
}
