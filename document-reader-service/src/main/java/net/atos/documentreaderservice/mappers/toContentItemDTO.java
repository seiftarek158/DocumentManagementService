package net.atos.documentreaderservice.mappers;


import net.atos.documentreaderservice.dto.LoadDirectoryDTO;
import net.atos.documentreaderservice.dto.DirectoryDTO;
import net.atos.documentreaderservice.dto.DocumentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface toContentItemDTO {
    LoadDirectoryDTO toDto(DirectoryDTO directoryDTO);
    LoadDirectoryDTO toDto(DocumentDTO documentDTO);

}
