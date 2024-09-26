package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.dto.DirectoryDTO;
import net.atos.documentreaderservice.dto.DocumentDTO;
import net.atos.documentreaderservice.mappers.toDirectoryDto;
import net.atos.documentreaderservice.mappers.toDocumentDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.repo.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class DirectoryQueryService {
    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private toDirectoryDto toDirectoryDto;

    @Autowired
    private toDocumentDto toDocumentDto;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DirectoryDocumentService directoryDocumentService;

    public List<DirectoryDTO> getDirectorysByNid(String nationalId) {

        return toDirectoryDto.toDtoList(directoryRepository.findByNationalidAndIsDeletedFalseAndParentIdIsNull(nationalId));
//        return directoryRepository.findByNationalidAndIsDeletedFalseAndParentIdIsNull(nationalId);
    }

    public List<DirectoryDTO> getNestedDirectories(UUID parentId) {
       return toDirectoryDto.toDtoList(directoryRepository.findByParentIdAndIsDeletedFalse(parentId));
//        return directoryRepository.findByParentIdAndIsDeletedFalse(parentId);
    }

    public List<DocumentDTO> getAllDocumentsInDirectory(UUID directoryId) {
        return toDocumentDto.toDtoList(directoryDocumentService.getAllDocumentsInDirectory(directoryId));

    }

    public DirectoryDTO getDirectoryById(UUID id) {
        Directory directory = directoryRepository.findByIdAndIsDeletedFalse(id);
        return toDirectoryDto.toDto(directory);
    }



}
