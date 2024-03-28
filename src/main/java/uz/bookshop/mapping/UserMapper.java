package uz.bookshop.mapping;


import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.domain.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapping<User, UserRequestDto, UserResponseDTO> {

}
