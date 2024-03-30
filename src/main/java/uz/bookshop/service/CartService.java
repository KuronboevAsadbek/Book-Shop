package uz.bookshop.service;

import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.response_dto.CartResponseDTO;
import uz.bookshop.domain.dto.response_dto.DeleteResponse;

import java.util.List;

public interface CartService {

    CartResponseDTO addToBasket(CartRequestDTO cartRequestDTO);

    List<CartResponseDTO> openBasket();

    DeleteResponse deleteCarts();

    DeleteResponse deleteOneCart(Long id);


}
