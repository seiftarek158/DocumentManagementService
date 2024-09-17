package net.atos.documentreaderservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.atos.documentreaderservice.dto.DirectoryDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.service.DirectoryService;
import net.atos.documentreaderservice.service.TokenService;
import net.atos.documentreaderservice.validation.createValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/directory")

public class DirectoryController {

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private TokenService tokenService;



    @GetMapping("/user")
    public ResponseEntity<List<Directory>> getDirectorysByNid(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        tokenService.validateToken(token);
        return ResponseEntity.ok(directoryService.getDirectorysByNid(token));
    }

    @GetMapping("/nested/{id}")
    public ResponseEntity<List<Directory>> getNestedDirectories(@PathVariable UUID id) {

        return ResponseEntity.ok(directoryService.getNestedDirectories(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Document>> getDocumentsByParentId(@PathVariable UUID id) {
        return ResponseEntity.ok(directoryService.getAllDocumentsInDirectory(id));
    }



}
