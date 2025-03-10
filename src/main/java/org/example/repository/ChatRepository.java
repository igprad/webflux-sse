/* igprad - (C) 2025 */
package org.example.repository;

import java.util.UUID;
import org.example.repository.entity.ChatEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatRepository extends ReactiveMongoRepository<ChatEntity, UUID> {}
