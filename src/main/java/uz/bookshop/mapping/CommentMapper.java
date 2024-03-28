package uz.bookshop.mapping;

import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.request_dto.CommentRequestDTO;
import uz.bookshop.domain.dto.response_dto.CommentResponseDTO;
import uz.bookshop.domain.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapping<Comment, CommentRequestDTO, CommentResponseDTO>  {
}
