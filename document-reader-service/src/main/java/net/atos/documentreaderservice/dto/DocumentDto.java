package net.atos.documentreaderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import net.atos.documentreaderservice.validation.createValidation;

import java.util.UUID;

@Data
public class DocumentDto {
    @NotBlank(message = "Name is mandatory",groups = {createValidation.class})
    private String name;
    @NotBlank(message = "Path is mandatory",groups = {createValidation.class})
    private String path;
    @NotBlank(message = "WorkspaceId is mandatory",groups = {createValidation.class})
    private UUID workspaceId;
    @NotBlank(message="ParentId is mandatory",groups = {createValidation.class})
    private UUID parentId;

}
