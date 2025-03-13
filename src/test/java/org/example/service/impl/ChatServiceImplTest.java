/* igprad - (C) 2025 */
package org.example.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.example.controller.model.ChatRequest;
import org.example.repository.ChatHistoryRepository;
import org.example.repository.ChatRepository;
import org.example.repository.entity.ChatEntity;
import org.example.repository.entity.ChatHistoryEntity;
import org.example.service.ChatService;
import org.example.service.model.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatHistoryRepository chatHistoryRepository;

    @BeforeEach
    void setup() {
        chatService = new ChatServiceImpl(
                chatRepository, chatHistoryRepository, Schedulers.immediate(), Schedulers.immediate());
    }

    @Test
    void streamChat() {
        ChatEntity chatEntity = mock();
        when(chatEntity.isShown()).thenReturn(false, true);
        List<ChatEntity> chatEntities = List.of(chatEntity);
        when(chatRepository.findAll()).thenReturn(Flux.fromIterable(chatEntities));
        when(chatRepository.save(any(ChatEntity.class))).thenReturn(Mono.fromCallable(() -> chatEntity));

        StepVerifier.withVirtualTime(chatService::streamChat)
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(Chat.fromEntity(chatEntity))
                .thenCancel()
                .verify();
    }

    @Test
    void chat() {
        ChatEntity chatEntity = mock();
        when(chatRepository.insert(any(ChatEntity.class))).thenReturn(Mono.fromCallable(() -> chatEntity));

        ChatRequest chatRequest = mock();
        StepVerifier.create(chatService.chat(chatRequest)).expectNext(true).verifyComplete();
    }

    @Test
    void archiveChat() {
        long epochMilli = Instant.now().toEpochMilli();
        ChatEntity chat = mock();
        List<ChatEntity> chatEntities = List.of(chat);
        when(chatRepository.findAllByCreatedOnLessThan(epochMilli)).thenReturn(Flux.fromIterable(chatEntities));
        ChatHistoryEntity chatHistory = mock();
        List<ChatHistoryEntity> chatHistoryEntities = List.of(chatHistory);
        when(chatHistoryRepository.saveAll(anyList())).thenReturn(Flux.fromIterable(chatHistoryEntities));
        when(chatRepository.deleteAll(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(chatService.archiveChat(epochMilli)).verifyComplete();
    }
}
