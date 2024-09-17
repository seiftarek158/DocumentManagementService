package net.atos.documentreaderservice.mappers;


import net.atos.documentreaderservice.dto.DirectoryDto;
import net.atos.documentreaderservice.model.Directory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

//@Component
//@Mapper
@Mapper(componentModel = "spring")
public interface toDirectoryDto {

//    toDirectoryDto INSTANCE = Mappers.getMapper(toDirectoryDto.class);


    Directory toEntity(DirectoryDto directoryDto);


    DirectoryDto toDto(Directory directory);

    @IterableMapping(elementTargetType = DirectoryDto.class)
    List<DirectoryDto> toDtoList(List<Directory> directories);
}
