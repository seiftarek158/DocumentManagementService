package net.atos.documentreaderservice.repo;

import net.atos.documentreaderservice.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {
     Document findByWorkspaceIdAndParentIdAndNameAndIsDeletedFalse(UUID workspaceId, UUID parentId, String name);
     List<Document> findByWorkspaceIdAndParentIdAndIsDeletedFalse(String workspaceId, String parentId);
     List<Document> findByWorkspaceIdAndIsDeletedFalse(UUID workspaceId);
     Document findByIdAndIsDeletedFalse(UUID id);
     List<Document>findByParentIdAndIsDeletedFalse(UUID parentId);

     Boolean existsByParentIdAndNameAndIsDeletedFalse(UUID parentId, String name);

     List<Document> findByWorkspaceIdAndNameContainingIgnoreCaseAndIsDeletedFalse(UUID workspaceId, String name);
}
