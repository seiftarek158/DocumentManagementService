package net.atos.documentreaderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoadDirectoryDTO{

    private DirectoryDTO parentDirectory;
    private List<DirectoryDTO> nested_directories;
    private List<DocumentDTO> nested_documents;

}
