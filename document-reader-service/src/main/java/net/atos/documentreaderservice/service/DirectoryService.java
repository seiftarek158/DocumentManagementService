package net.atos.documentreaderservice.service;

import net.atos.documentreaderservice.dto.DirectoryDTO;
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
public class DirectoryService {
    @Autowired
    private DirectoryRepository directoryRepository;
    

    @Autowired
    private toDirectoryDto toDirectoryDto;

    @Autowired
    private TokenService tokenService;


    @Value("${user.default.directory}")
    private String baseDirectory;





    public DirectoryDTO createDirectory(DirectoryDTO directorydto, String token){

        String nationalId = tokenService.extractNationalid(token);

        //validate directory
        validateDirectory(directorydto,nationalId);


       setWorkspaceIdIfNeeded(directorydto);

        //Set national id and create directory
        directorydto.setNationalid(nationalId);
        Directory directory = toDirectoryDto.toEntity(directorydto);

        //set path
       String path =determineDirectoryPath(directorydto,directory);

        directory.setPath(path);
        createPhysicalDirectory(path);
        directoryRepository.save(directory);
        DirectoryDTO directoryDTO= toDirectoryDto.toDto(directory);

        return directoryDTO;
    }


    private void setWorkspaceIdIfNeeded(DirectoryDTO directorydto) {
        if (directorydto.getParentId() != null && directorydto.getWorkspaceId() == null) {
            Directory parentDirectory = directoryRepository.findByIdAndIsDeletedFalse(directorydto.getParentId());
            directorydto.setWorkspaceId(parentDirectory.getId());
        }
    }

    private String determineDirectoryPath(DirectoryDTO directorydto, Directory directory) {
        if (directorydto.getParentId() != null) {
            Directory parentDirectory = directoryRepository.findByIdAndIsDeletedFalse(directorydto.getParentId());
            return parentDirectory.getPath() + "/" + directory.getId();
        } else {
            return baseDirectory + "/" + directory.getId();
        }
    }

    private void validateDirectory(DirectoryDTO directorydto, String nationalId) {
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
    }

    private void createPhysicalDirectory(String path) {
        try {
            Files.createDirectory(Path.of(path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create workspace directory");
        }
    }

    public DirectoryDTO updateDirectory(DirectoryDTO newdirectorydto, UUID id){
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
        directoryRepository.save(directory);
        return toDirectoryDto.toDto(directory);
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
