package uz.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.request_dto.PlusMinusRequest;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.dto.response_dto.Total;

public interface CartService {

    ResponseDTO addToBasket(CartRequestDTO cartRequestDTO, HttpServletRequest httpServletRequest);

    Total openBasket(HttpServletRequest httpServletRequest);


    void addOneOrMinusOne(PlusMinusRequest plusMinusRequest, HttpServletRequest httpServletRequest);

    ResponseDTO deleteCarts(HttpServletRequest httpServletRequest);

    ResponseDTO deleteOneCart(Long id, HttpServletRequest httpServletRequest);


}
