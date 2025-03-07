/* igprad - (C) 2025 */
package org.example.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface EventService {

    Flux<ServerSentEvent<String>> stringEventStream();
}
