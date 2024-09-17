package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.dto.DirectoryDto;
import net.atos.documentreaderservice.exception.DuplicateEntryException;
import net.atos.documentreaderservice.mappers.toDirectoryDto;
import net.atos.documentreaderservice.model.Directory;
import net.atos.documentreaderservice.repo.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;


@Service
public class DirectoryWriterService {
    @Autowired
    private DirectoryRepository directoryRepository;
    

    @Autowired
    private toDirectoryDto toDirectoryDto;

    @Autowired
    private TokenService tokenService;


    @Value("${user.default.directory}")
    private String baseDirectory;





    public Directory createDirectory(DirectoryDto directorydto, String token){

        String nationalId = tokenService.extractNationalid(token);

        //check if directory already exists and if valid parent directory
        if(directorydto.getParentId()==null){
            if(directoryRepository.findByNationalidAndNameAndIsDeletedFalse(nationalId,directorydto.getName()) !=null){
                throw new DuplicateEntryException("Directory already exists");
            }

        }
        else{

            if(directoryRepository.findByIdAndIsDeletedFalse(directorydto.getParentId())==null){
                throw new RuntimeException("Parent directory not found");
            }
            if(directoryRepository.findByNameAndParentIdAndIsDeletedFalse(directorydto.getName(),directorydto.getParentId()) !=null){
                throw new DuplicateEntryException("Directory already exists");
            }
        }

        //case when the directory is a workspace parentId is null and workspaceId is its own id
        //case when the directory is directly a subworkspace parentId  is parent workspace id and workspaceId is own id
        //case when the directory is a subdirectory parentId is parent directory id and workspaceId is parent workspace id

        //case when the directory has a not null parentID but null workspaceId
        System.out.println("directory parentId: "+ directorydto.getParentId());
        System.out.println("directory workspaceId: "+ directorydto.getWorkspaceId());

        if(directorydto.getParentId()!=null && directorydto.getWorkspaceId()==null){
            Directory parentDirectory = directoryRepository.findByIdAndIsDeletedFalse(directorydto.getParentId());
            System.out.println("parent directorys Id: "+ parentDirectory.getId() );
            directorydto.setWorkspaceId(parentDirectory.getId());
        }

        //Set national id and create directory
        directorydto.setNationalid(nationalId);
        Directory directory = toDirectoryDto.toEntity(directorydto);

        //set path
        String path;
        if(directorydto.getParentId()!=null){
            Directory ParentDirectory = directoryRepository.findByIdAndIsDeletedFalse(directorydto.getParentId());
            path = ParentDirectory.getPath()+ "/" + directory.getId();
            directory.setPath(path);
        }
        else{
            path = baseDirectory + "/"+ directory.getId();
        }

        directory.setPath(path);
        createDirectoryDirectory(path);
        return directoryRepository.save(directory);
    }

    private void createDirectoryDirectory(String path) {
        try {
            Files.createDirectory(Path.of(path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create workspace directory");
        }
    }

    public Directory updateDirectory(DirectoryDto newdirectorydto, UUID id){
        Directory directory = directoryRepository.findByIdAndIsDeletedFalse(id);
        if(directory ==null){
            throw new RuntimeException("Directory not found");
        }
        if(newdirectorydto.getName()!=null){
         directory.setName(newdirectorydto.getName());
         updateDirectoryPath(directory,newdirectorydto.getName());
        }
        if(newdirectorydto.getDescription()!=null){
            directory.setDescription(newdirectorydto.getDescription());
        }

        return directoryRepository.save(directory);
    }

    private void updateDirectoryPath(Directory directory, String newName) {
        String oldPath = directory.getPath();
        String parentPath = oldPath.substring(0, oldPath.lastIndexOf('/'));
        String newPath = parentPath + "/" + newName;
        directory.setPath(newPath);
    }

    public void deleteDirectory(UUID id){
        Directory directory = Optional.ofNullable(directoryRepository.findByIdAndIsDeletedFalse(id)).orElseThrow(() -> new RuntimeException("Directory not found"));
        directory.setDeleted(true);
        directoryRepository.save(directory);
    }

}
