/* igprad - (C) 2025 */
package org.example.repository;

import java.util.UUID;
import org.example.repository.entity.ChatHistoryEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatHistoryRepository extends ReactiveMongoRepository<ChatHistoryEntity, UUID> {}
