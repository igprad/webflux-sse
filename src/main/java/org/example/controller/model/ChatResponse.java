/* igprad - (C) 2025 */
package org.example.controller.model;

import java.time.Instant;
import java.util.Date;
import org.example.service.model.Chat;

public record ChatResponse(String message, String username, Date messagedOn) {

    public static ChatResponse fromService(Chat chat) {
        return new ChatResponse(chat.message(), chat.username(), Date.from(Instant.ofEpochMilli(chat.messagedOn())));
    }
}
