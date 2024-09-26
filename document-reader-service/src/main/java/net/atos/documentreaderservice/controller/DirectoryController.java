package net.atos.documentreaderservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.atos.documentreaderservice.dto.DirectoryDTO;
import net.atos.documentreaderservice.dto.DocumentDTO;
import net.atos.documentreaderservice.dto.LoadDirectoryDTO;
import net.atos.documentreaderservice.service.DirectoryQueryService;
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
@RequestMapping("/directories")

public class DirectoryController {

    @Autowired
    private DirectoryQueryService directoryQueryService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DirectoryService directoryService;



    @GetMapping()
    public ResponseEntity<List<DirectoryDTO>> getDirectorysByNid(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        tokenService.validateToken(token);
        String nationalId = tokenService.extractNationalid(token);
        return ResponseEntity.ok(directoryQueryService.getDirectorysByNid(nationalId));
    }

    @GetMapping("/{directoryId}/nested")
    public ResponseEntity<List<DirectoryDTO>> getNestedDirectories(@PathVariable UUID directoryId) {

        return ResponseEntity.ok(directoryQueryService.getNestedDirectories(directoryId));
    }

    @GetMapping("/{parentId}/documents")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByParentId(@PathVariable UUID parentId) {
        return ResponseEntity.ok(directoryQueryService.getAllDocumentsInDirectory(parentId));
    }




    @PostMapping()
    public ResponseEntity<DirectoryDTO> createDirectory(@RequestBody @Validated(createValidation.class) DirectoryDTO workspace, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        if(!tokenService.validateToken(token)){
            throw new RuntimeException("Invalid token");
        }

        return ResponseEntity.ok(directoryService.createDirectory(workspace,token));
    }


    @DeleteMapping("/{directoryId}")
    public ResponseEntity<Void> deleteDirectory(@Valid @PathVariable  UUID directoryId) {
        directoryService.deleteDirectory(directoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{directoryId}")
    public ResponseEntity<DirectoryDTO> updateDirectory(@PathVariable UUID directoryId, @RequestBody DirectoryDTO directory) {
        DirectoryDTO updatedDirectory = directoryService.updateDirectory(directory, directoryId);
        return ResponseEntity.ok(updatedDirectory);
    }



    @GetMapping("/{directoryId}")
    public ResponseEntity<LoadDirectoryDTO> getDirectoryById(@PathVariable UUID directoryId) {
        LoadDirectoryDTO loadDirectoryDTO = new LoadDirectoryDTO();
        loadDirectoryDTO.setParentDirectory(directoryQueryService.getDirectoryById(directoryId));
        loadDirectoryDTO.setNested_directories(directoryQueryService.getNestedDirectories(directoryId));
        loadDirectoryDTO.setNested_documents(directoryQueryService.getAllDocumentsInDirectory(directoryId));
        return ResponseEntity.ok(loadDirectoryDTO);
    }






}
