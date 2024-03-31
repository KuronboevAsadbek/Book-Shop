package uz.bookshop.service;

import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;

public interface OrderService {

    OrderResponseDTO createOrder();

    ResponseDTO deleteOrder();
}
