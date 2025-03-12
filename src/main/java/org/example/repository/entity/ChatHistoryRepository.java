/* igprad - (C) 2025 */
package org.example.repository.entity;

import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatHistoryRepository extends ReactiveMongoRepository<ChatHistoryEntity, UUID> {}
