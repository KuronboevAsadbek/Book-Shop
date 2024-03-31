package uz.bookshop.service;

import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.response_dto.CartResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;

import java.util.List;

public interface CartService {

    CartResponseDTO addToBasket(CartRequestDTO cartRequestDTO);

    List<CartResponseDTO> openBasket();

    ResponseDTO deleteCarts();

    ResponseDTO deleteOneCart(Long id);


}
