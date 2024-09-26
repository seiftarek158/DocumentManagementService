package net.atos.documentreaderservice.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "directories")
public class Directory {
    @Id
    private  UUID id=UUID.randomUUID();
    @NotBlank(message = "Nationalid is mandatory")
    private String nationalid;
    private String name;
    private String path;
    @CreatedDate
    private LocalDateTime createdAt=LocalDateTime.now();
    private boolean isDeleted;
    String description;
    private UUID parentId;
    private UUID workspaceId;

}
