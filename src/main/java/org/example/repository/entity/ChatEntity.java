/* igprad - (C) 2025 */
package org.example.repository.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chat")
public class ChatEntity {
    @Id
    private UUID id;

    private String message;
    private String username;
    private boolean shown;

    @CreatedDate
    private Long createdOn;

    @LastModifiedDate
    private Long modifiedOn;

    @Version
    private Long version;
}
