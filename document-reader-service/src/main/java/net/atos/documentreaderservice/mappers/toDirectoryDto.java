package net.atos.documentreaderservice.mappers;


import net.atos.documentreaderservice.dto.DirectoryDTO;
import net.atos.documentreaderservice.model.Directory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

//@Component
//@Mapper
@Mapper(componentModel = "spring")
public interface toDirectoryDto {

//    toDirectoryDto INSTANCE = Mappers.getMapper(toDirectoryDto.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Directory toEntity(DirectoryDTO directoryDto);
    DirectoryDTO toDto(Directory directory);
    List<DirectoryDTO> toDtoList(List<Directory> directoryList);

}
