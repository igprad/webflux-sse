/* igprad - (C) 2025 */
package org.example.service.impl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.model.ChatRequest;
import org.example.repository.ChatRepository;
import org.example.repository.entity.ChatEntity;
import org.example.repository.entity.ChatHistoryEntity;
import org.example.repository.entity.ChatHistoryRepository;
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
    private final ChatHistoryRepository chatHistoryRepository;
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
        return chatRepository
                .insert(this.createNewChat(chatRequest.message(), chatRequest.username()))
                .map(ignored -> Boolean.TRUE)
                .publishOn(repositoryScheduler)
                .subscribeOn(commonScheduler);
    }

    private ChatEntity createNewChat(String message, String username) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(UUID.randomUUID());
        chatEntity.setMessage(message);
        chatEntity.setUsername(username);
        chatEntity.setShown(false);
        return chatEntity;
    }

    @Override
    public Mono<Void> archiveChat(Long timestamp) {
        return chatRepository
                .findAllByCreatedOnLessThan(timestamp)
                .collectList()
                .flatMap(oldChats -> {
                    List<ChatHistoryEntity> newChatHistories =
                            oldChats.stream().map(this::createNewChatHistory).toList();
                    return this.chatHistoryRepository
                            .saveAll(newChatHistories)
                            .flatMap(ignored -> this.chatRepository.deleteAll(oldChats))
                            .then();
                })
                .publishOn(repositoryScheduler)
                .subscribeOn(commonScheduler);
    }

    private ChatHistoryEntity createNewChatHistory(ChatEntity chat) {
        ChatHistoryEntity chatHistory = new ChatHistoryEntity();
        chatHistory.setId(UUID.randomUUID());
        chatHistory.setMessage(chat.getMessage());
        chatHistory.setUsername(chat.getUsername());
        chatHistory.setShown(true);
        return chatHistory;
    }
}
