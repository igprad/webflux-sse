/* igprad - (C) 2025 */
package org.example.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.model.ChatRequest;
import org.example.repository.ChatRepository;
import org.example.repository.entity.ChatEntity;
import org.example.service.ChatService;
import org.example.service.model.Chat;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final Scheduler repositoryScheduler;
    private final Scheduler commonScheduler;

    @Override
    public Flux<Chat> streamChat() {
        return Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).flatMap(ignored -> chatRepository
                .findAll()
                .filter(chatEntity -> !chatEntity.isShown())
                .map(chatEntity -> {
                    chatEntity.setShown(true);
                    chatRepository
                            .save(chatEntity)
                            .subscribeOn(repositoryScheduler)
                            .subscribe();
                    return Chat.fromEntity(chatEntity);
                })
                .publishOn(repositoryScheduler)
                .subscribeOn(commonScheduler));
    }

    @Override
    public Mono<Boolean> chat(ChatRequest chatRequest) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(UUID.randomUUID());
        chatEntity.setMessage(chatRequest.message());
        chatEntity.setUsername(chatRequest.username());
        long epochMilli = Instant.now().toEpochMilli();
        chatEntity.setCreatedOn(epochMilli);
        chatEntity.setModifiedOn(epochMilli);
        chatEntity.setShown(false);
        return chatRepository
                .insert(chatEntity)
                .map(ignored -> Boolean.TRUE)
                .publishOn(repositoryScheduler)
                .subscribeOn(commonScheduler);
    }
}
