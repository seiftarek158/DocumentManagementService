package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.dto.DirectoryDto;
import net.atos.documentreaderservice.exception.DuplicateEntryException;
import net.atos.documentreaderservice.mappers.toDirectoryDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.repo.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class DirectoryService {
    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private toDirectoryDto toDirectoryDto;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DirectoryDocumentService directoryDocumentService;

    public List<Directory> getDirectorysByNid(String token) {
        String nationalId = tokenService.extractNationalid(token);

        return directoryRepository.findByNationalidAndIsDeletedFalseAndParentIdIsNull(nationalId);
    }

    public List<Directory> getNestedDirectories(UUID parentId) {
        return directoryRepository.findByParentIdAndIsDeletedFalse(parentId);
    }

    public List<Document> getAllDocumentsInDirectory(UUID directoryId) {
        return directoryDocumentService.getAllDocumentsInDirectory(directoryId);
    }

}
