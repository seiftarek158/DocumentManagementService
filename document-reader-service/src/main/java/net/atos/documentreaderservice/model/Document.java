package net.atos.documentreaderservice.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {
    @Id
    private UUID id=UUID.randomUUID();
    private String name;
    private String path;
    @CreatedDate
    private LocalDateTime createdAt=LocalDateTime.now();;
    private boolean isDeleted;
    private UUID workspaceId;
    private UUID parentId;

}
