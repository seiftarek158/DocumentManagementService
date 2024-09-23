package net.atos.documentreaderservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.atos.documentreaderservice.dto.DirectoryDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.service.DirectoryReaderService;
import net.atos.documentreaderservice.service.DirectoryWriterService;
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
    private DirectoryReaderService directoryReaderService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DirectoryWriterService directoryWriterService;



    @GetMapping("/user")
    public ResponseEntity<List<Directory>> getDirectorysByNid(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        tokenService.validateToken(token);
        return ResponseEntity.ok(directoryReaderService.getDirectorysByNid(token));
    }

    @GetMapping("/nested/{id}")
    public ResponseEntity<List<Directory>> getNestedDirectories(@PathVariable UUID id) {

        return ResponseEntity.ok(directoryReaderService.getNestedDirectories(id));
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<List<Document>> getDocumentsByParentId(@PathVariable UUID id) {
        return ResponseEntity.ok(directoryReaderService.getAllDocumentsInDirectory(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Directory> getDirectoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(directoryReaderService.getDirectoryById(id));
    }


    @PostMapping("/new")
    public ResponseEntity<Directory> createDirectory(@RequestBody @Validated(createValidation.class) DirectoryDto workspace, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        if(!tokenService.validateToken(token)){
            throw new RuntimeException("Invalid token");
        }
        return ResponseEntity.ok(directoryWriterService.createDirectory(workspace,token));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirectory(@Valid @PathVariable  UUID id) {
        directoryWriterService.deleteDirectory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Directory> updateDirectory(@PathVariable UUID id, @RequestBody DirectoryDto directory) {
        Directory updatedDirectory = directoryWriterService.updateDirectory(directory, id);
        return ResponseEntity.ok(updatedDirectory);
    }






}
