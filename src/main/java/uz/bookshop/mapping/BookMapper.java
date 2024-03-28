package uz.bookshop.mapping;


import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.request_dto.BookRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapping<Book, BookRequestDTO, BookResponseDTO> {

}
