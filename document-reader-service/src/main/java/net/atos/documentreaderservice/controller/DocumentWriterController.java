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










    
}
