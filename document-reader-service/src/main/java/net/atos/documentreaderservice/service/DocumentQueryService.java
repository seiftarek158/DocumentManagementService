package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.dto.DocumentDTO;
import net.atos.documentreaderservice.mappers.toDocumentDto;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.repo.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentQueryService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private toDocumentDto toDocumentDto;




    public Resource downloadDocument(UUID id){
        Document document = documentRepository.findByIdAndIsDeletedFalse(id);
        if(document == null){
            throw new RuntimeException("Document not found");
        }
        String filepath = document.getPath();
        Path path = Paths.get(filepath);
        File file = path.toFile();
        if (!file.exists() || !file.isFile()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + filepath);
        }
        return new FileSystemResource(file);
    }



    public String getContentType(Resource resource) {
        try {
            Path filePath = resource.getFile().toPath();
            return Files.probeContentType(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not determine file type.", ex);
        }
    }

    public List<DocumentDTO> searchDocuments(UUID workspaceId, String name){
        List<Document> documents= documentRepository.findByWorkspaceIdAndNameContainingIgnoreCaseAndIsDeletedFalse(workspaceId, name);
        return toDocumentDto.toDtoList(documents);
    }


}
