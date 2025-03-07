/* igprad - (C) 2025 */
package org.example.controller;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class EventController {

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).map(this::construct);
    }

    private ServerSentEvent<String> construct(Long second) {
        return ServerSentEvent.<String>builder()
                .event("webflux-sse-event-test")
                .data("Streamed event in " + second + " second.")
                .comment("test")
                .build();
    }
}
