package net.atos.documentreaderservice.repo;

import net.atos.documentreaderservice.model.Directory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface DirectoryRepository extends MongoRepository<Directory, UUID> {
    //for checking duplicate workspaces names
    Directory findByNationalidAndNameAndIsDeletedFalse(String nationalid, String name);

    //for checking duplicate directory  names inside workspace
    Directory findByNameAndParentIdAndIsDeletedFalse(String name, UUID parentId);

    //for ADMIN
    List<Directory> findByIsDeletedFalse();

    List<Directory> findByNationalidAndIsDeletedFalseAndParentIdIsNull(String nationalid);

    Directory findByIdAndIsDeletedFalse(UUID id);



    List<Directory> findByParentIdAndIsDeletedFalse(UUID parentId);


}


