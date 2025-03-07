/* igprad - (C) 2025 */
package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final Scheduler commonScheduler;

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        return eventService.stringEventStream().subscribeOn(commonScheduler);
    }
}
