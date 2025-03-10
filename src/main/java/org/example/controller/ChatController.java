/* igprad - (C) 2025 */
package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.model.ChatRequest;
import org.example.controller.model.ChatResponse;
import org.example.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final Scheduler commonScheduler;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> streamChats() {
        return this.chatService
                .streamChat()
                .map(ChatResponse::fromService)
                .subscribeOn(commonScheduler)
                .publishOn(commonScheduler);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Boolean> chat(@RequestBody @Valid ChatRequest chatRequest) {
        return chatService.chat(chatRequest).subscribeOn(commonScheduler).publishOn(commonScheduler);
    }
}
