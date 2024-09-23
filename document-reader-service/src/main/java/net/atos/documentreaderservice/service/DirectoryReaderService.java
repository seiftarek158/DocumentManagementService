package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.mappers.toDirectoryDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.repo.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class DirectoryReaderService {
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

    public Directory getDirectoryById(UUID id) {
        return directoryRepository.findByIdAndIsDeletedFalse(id);
    }

}
