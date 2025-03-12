/* igprad - (C) 2025 */
package org.example.repository;

import java.util.UUID;
import org.example.repository.entity.ChatEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<ChatEntity, UUID> {

    Flux<ChatEntity> findAllByCreatedOnLessThan(Long epochInMillis);
}
