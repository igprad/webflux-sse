/* igprad - (C) 2025 */
package org.example.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.example.controller.model.ChatRequest;
import org.example.controller.model.ChatResponse;
import org.example.service.ChatService;
import org.example.service.model.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ChatController.class)
class ChatControllerTest {

    @MockitoBean
    private ChatService chatService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        this.webTestClient =
                WebTestClient.bindToController(new ChatController(chatService)).build();
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
        given(this.chatService.chat(chatRequest)).willReturn(Mono.fromCallable(() -> Boolean.TRUE));
        webTestClient
                .post()
                .uri("/chat")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(chatRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .returnResult();
    }
}
