/* igprad - (C) 2025 */
package org.example.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import org.example.controller.model.ChatRequest;
import org.example.controller.model.ChatResponse;
import org.example.service.ChatService;
import org.example.service.model.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@WebFluxTest(controllers = ChatController.class)
class ChatControllerTest {

    @MockitoBean
    private ChatService chatService;

    @MockitoBean
    private Scheduler testScheduler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        given(testScheduler.createWorker())
                .willReturn(Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor())
                        .createWorker());
        this.webTestClient = WebTestClient.bindToController(new ChatController(chatService, testScheduler))
                .build()
                .mutate()
                .responseTimeout(Duration.of(3, ChronoUnit.SECONDS))
                .build();
    }

    @Test
    void subscribeChats() {
        Chat chat = mock();
        List<Chat> chats = List.of(chat);
        given(this.chatService.streamChat()).willReturn(Flux.fromIterable(chats));
        webTestClient
                .get()
                .uri("/chat")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(ChatResponse.class)
                .returnResult();
    }

    @Test
    void chat() {
        ChatRequest chatRequest = new ChatRequest("test", "test");
        given(this.chatService.chat(chatRequest)).willReturn(Mono.just(true));
        webTestClient
                .post()
                .uri("/chat")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(chatRequest))
                .exchange()
                .expectStatus()
                .isCreated();
    }
}
