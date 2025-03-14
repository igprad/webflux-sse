/* igprad - (C) 2025 */
package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.model.ChatRequest;
import org.example.controller.model.ChatResponse;
import org.example.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public Flux<ChatResponse> subscribeChats() {
        return this.chatService.streamChat().map(ChatResponse::fromService).subscribeOn(commonScheduler);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> chat(@RequestBody @Valid ChatRequest chatRequest) {
        return chatService
                .chat(chatRequest)
                .map(result -> this.<Void>constructResponse(HttpStatus.CREATED, null))
                .subscribeOn(commonScheduler);
    }

    private <T> ResponseEntity<T> constructResponse(HttpStatus httpStatus, T body) {
        return ResponseEntity.status(httpStatus).body(body);
    }
}
