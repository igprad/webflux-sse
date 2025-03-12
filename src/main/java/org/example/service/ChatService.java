/* igprad - (C) 2025 */
package org.example.service;

import org.example.controller.model.ChatRequest;
import org.example.service.model.Chat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatService {

    Flux<Chat> streamChat();

    Mono<Boolean> chat(ChatRequest chatRequest);

    Mono<Void> archiveChat(Long timestamp);
}
