package net.atos.documentreaderservice.controller;

import jakarta.validation.Valid;
import net.atos.documentreaderservice.service.DocumentWriterService;
import net.atos.documentreaderservice.dto.DocumentDto;
import net.atos.documentreaderservice.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
public class DocumentWriterController {
    @Autowired
    private DocumentWriterService documentService;


    @PostMapping("/{parentId}/upload")
    public ResponseEntity<Map<String,String>> uploadDocument(@PathVariable String parentId, @RequestParam("file") MultipartFile file) {

        HashMap<String, String> response = new HashMap<>() ;
        try {
            UUID parentIdUUID = UUID.fromString(parentId);
            Document document = documentService.uploadDocument(parentIdUUID, file);
            response.put("message", "Document uploaded successfully: " + document);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.put("message", "File upload failed: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<Void> deleteDocument(@Valid @PathVariable UUID documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/document/{documentId}")
    public ResponseEntity<Document> updateDocument(@PathVariable UUID documentId, @RequestBody DocumentDto document) {
        Document updatedDocument = documentService.updateDocument(document, documentId);
        return ResponseEntity.ok(updatedDocument);
    }











    
}
