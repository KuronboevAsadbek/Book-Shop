package uz.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;

public interface OrderService {

    OrderResponseDTO createOrder(HttpServletRequest httpServletRequest);

    ResponseDTO deleteOrder();
}
