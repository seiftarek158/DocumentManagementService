package net.atos.documentreaderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import net.atos.documentreaderservice.validation.createValidation;

import java.util.UUID;

@Data
public class DirectoryDto {

    private String nationalid;
    @NotBlank(message = "Name is mandatory",groups = {createValidation.class})
    private String name;
    private String description;
    private UUID parentId;
    private UUID workspaceId;
}
