package net.atos.documentreaderservice.controller;

import jakarta.validation.Valid;
import net.atos.documentreaderservice.dto.DocumentDTO;
import net.atos.documentreaderservice.service.DocumentQueryService;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class DocumentController {
    @Autowired
    private DocumentQueryService documentQueryService;

    @Autowired
    private DocumentService documentService;



    @GetMapping("/documents/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID documentId) {
        Resource file = documentQueryService.downloadDocument(documentId);
        String contentType = documentQueryService.getContentType(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


    @GetMapping("/searchDocuments")
    public ResponseEntity<List<DocumentDTO>> searchDocuments(@RequestParam String searchTerm, @RequestParam UUID workspaceId) {
        return ResponseEntity.ok(documentQueryService.searchDocuments(workspaceId, searchTerm));
    }


    @PostMapping("/{directoryId}/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(@PathVariable String directoryId, @RequestParam("file") MultipartFile file) {

        try {
            UUID parentIdUUID = UUID.fromString(directoryId);
            DocumentDTO documentDTO = documentService.uploadDocument(parentIdUUID, file);

            return ResponseEntity.ok(documentDTO);
        } catch (IOException e) {


            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(@Valid @PathVariable UUID documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/documents/{documentId}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable UUID documentId, @RequestBody DocumentDTO document) {
        DocumentDTO updatedDocument = documentService.updateDocument(document, documentId);
        return ResponseEntity.ok(updatedDocument);
    }















}
