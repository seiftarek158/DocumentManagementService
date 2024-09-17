package net.atos.documentreaderservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.atos.documentreaderservice.service.DirectoryWriterService;
import net.atos.documentreaderservice.dto.DirectoryDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.service.DirectoryService;
import net.atos.documentreaderservice.service.TokenService;
import net.atos.documentreaderservice.validation.createValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/directory")

public class DirectoryWriterController {

    @Autowired
    private DirectoryWriterService directoryService;

    @Autowired
    private TokenService tokenService;



    @PostMapping("/new")
    public ResponseEntity<Directory> createDirectory(@RequestBody @Validated(createValidation.class) DirectoryDto workspace, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        if(!tokenService.validateToken(token)){
            throw new RuntimeException("Invalid token");
        }
        return ResponseEntity.ok(directoryService.createDirectory(workspace,token));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirectory(@Valid @PathVariable  UUID id) {
        directoryService.deleteDirectory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Directory> updateDirectory(@PathVariable UUID id, @RequestBody DirectoryDto directory) {
        Directory updatedDirectory = directoryService.updateDirectory(directory, id);
        return ResponseEntity.ok(updatedDirectory);
    }




}
