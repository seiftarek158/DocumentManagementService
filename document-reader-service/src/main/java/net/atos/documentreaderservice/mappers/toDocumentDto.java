package net.atos.documentreaderservice.mappers;


import net.atos.documentreaderservice.dto.DocumentDTO;
import net.atos.documentreaderservice.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

//@Component
//@Mapper
@Mapper(componentModel = "spring")
public interface toDocumentDto {
    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Document toEntity(DocumentDTO documentDto);
    DocumentDTO toDto(Document document);
    List<DocumentDTO> toDtoList(List<Document> documentList);

}
