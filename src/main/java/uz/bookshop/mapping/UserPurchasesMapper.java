package uz.bookshop.mapping;

import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.request_dto.UserPurchasesRequestDTO;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.domain.model.UserPurchases;

@Mapper(componentModel = "spring")
public interface UserPurchasesMapper extends EntityMapping<UserPurchases, UserPurchasesRequestDTO, UserResponseDTO> {

}
