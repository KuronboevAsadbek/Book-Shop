package uz.bookshop.mapping;

import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.response_dto.CartResponseDTO;
import uz.bookshop.domain.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper extends EntityMapping<Cart, CartRequestDTO, CartResponseDTO> {

}
