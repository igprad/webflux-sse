/* igprad - (C) 2025 */
package org.example.service.model;

import org.example.repository.entity.ChatEntity;

public record Chat(String message, String username, Long messagedOn) {

    public static Chat fromEntity(ChatEntity chatEntity) {
        return new Chat(chatEntity.getMessage(), chatEntity.getUsername(), chatEntity.getCreatedOn());
    }
}
