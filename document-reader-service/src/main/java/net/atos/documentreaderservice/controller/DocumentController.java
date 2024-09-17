package net.atos.documentreaderservice.controller;

import jakarta.validation.Valid;
import net.atos.documentreaderservice.dto.DocumentDto;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
public class DocumentController {
    @Autowired
    private DocumentService documentService;




    @GetMapping("/document/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID documentId) {
        Resource file = documentService.downloadDocument(documentId);
        String contentType = documentService.getContentType(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


    @GetMapping("/{workspaceId}/documents")
    public ResponseEntity<List<Document>> searchDocuments(@PathVariable UUID workspaceId, @RequestParam String searchTerm) {
        return ResponseEntity.ok(documentService.searchDocuments(workspaceId, searchTerm));
    }










    
}
