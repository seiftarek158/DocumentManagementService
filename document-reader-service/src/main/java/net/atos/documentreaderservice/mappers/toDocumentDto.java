package net.atos.documentreaderservice.mappers;


import net.atos.documentreaderservice.dto.DocumentDto;
import net.atos.documentreaderservice.model.Document;
import org.mapstruct.Mapper;

//@Component
//@Mapper
@Mapper(componentModel = "spring")
public interface toDocumentDto {
    Document toEntity(DocumentDto documentDto);
}
