/* igprad - (C) 2025 */
package org.example.repository.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// todo : enable auditor
@Data
@Document(collection = "chat")
public class ChatEntity {
    @Id
    private UUID id;

    private String message;
    private String username;
    private Long createdOn;
    private Long modifiedOn;
    private boolean shown;
}
