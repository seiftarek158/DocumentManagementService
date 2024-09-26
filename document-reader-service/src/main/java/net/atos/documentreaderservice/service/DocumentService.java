package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.dto.DocumentDTO;
import net.atos.documentreaderservice.mappers.toDocumentDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.model.Document;
import net.atos.documentreaderservice.repo.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private toDocumentDto toDocumentDto;

    @Autowired
    private DirectoryDocumentService directoryDocumentService;



    public DocumentDTO uploadDocument(UUID parentId, MultipartFile file) throws IOException {
        String path="";
        DocumentDTO document=new DocumentDTO();
        document.setParentId(parentId);
        document.setName(file.getOriginalFilename());

        if(documentRepository.existsByParentIdAndNameAndIsDeletedFalse(parentId, document.getName())){
            throw new RuntimeException("Document already exists");
        }


        Directory parentDirectory = directoryDocumentService.getDirectoryById(parentId);




            if(parentDirectory.getWorkspaceId()==null){
                document.setWorkspaceId(parentDirectory.getId());
            }
            else{
                document.setWorkspaceId(parentDirectory.getWorkspaceId());
            }
            path= parentDirectory.getPath();




        path+= "/"+document.getName();
        document.setPath(path);
        //create a new document record
        Document newDocument=  createDocument(document);

        //save the file to the path
        file.transferTo(new File(path));

        return toDocumentDto.toDto(newDocument);
    }


    public Document createDocument(DocumentDTO document){
        Document newDocument=toDocumentDto.toEntity(document);
        return documentRepository.save(newDocument);
    }


    public void deleteDocument(UUID id){
        Document document = documentRepository.findByIdAndIsDeletedFalse(id);
        if(document == null){
            throw new RuntimeException("Document not found");
        }
        document.setDeleted(true);
        documentRepository.save(document);
    }

    public DocumentDTO updateDocument(DocumentDTO document, UUID id){
        Document existingDocument = documentRepository.findByIdAndIsDeletedFalse(id);
        if(existingDocument == null){
            throw new RuntimeException("Document not found");
        }
        String oldPath = existingDocument.getPath();

        String oldName=existingDocument.getName();

        if(document.getName()!=null){
            try {
                existingDocument.setName(document.getName());
                String  path=updateDocumentPath(existingDocument, document.getName());
                renameDocument(oldPath,path);

            }
            catch (Exception e){
                existingDocument.setName(oldName);
                existingDocument.setPath(oldPath);
                throw new RuntimeException("Failed to update document name"+e.getMessage());
            }


        }


        documentRepository.save(existingDocument);

        return toDocumentDto.toDto(existingDocument);
    }

    private void renameDocument(String oldPath, String newPath) {

        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (!oldFile.exists()) {
            throw new RuntimeException("Old file does not exist");
        }
        if (!oldFile.canWrite() || !newFile.getParentFile().canWrite()) {
            throw new RuntimeException("Insufficient permissions to rename file");
        }
        if (!oldFile.renameTo(newFile)) {
            throw new RuntimeException("Failed to rename file");
        }


    }

    private String updateDocumentPath(Document document, String newName) {
        String oldPath = document.getPath();
        String parentPath = oldPath.substring(0, oldPath.lastIndexOf('/'));
        String newPath = parentPath + "/" + newName;
        document.setPath(newPath);

        return newPath;

    }



}
