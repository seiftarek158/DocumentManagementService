package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.repo.DirectoryRepository;
import net.atos.documentreaderservice.repo.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DirectoryDocumentService {
    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> getAllDocumentsInDirectory(UUID directoryId) {
        return documentRepository.findByParentIdAndIsDeletedFalse(directoryId);
    }
    public Directory getDirectoryById(UUID id) {
        return directoryRepository.findByIdAndIsDeletedFalse(id);
    }


}
