package uz.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;

import java.util.List;

public interface OrderService {

    ResponseDTO createOrder(HttpServletRequest httpServletRequest);

    OrderResponseDTO getLastOrder(HttpServletRequest httpServletRequest);

    List<OrderResponseDTO> getAllOrders(HttpServletRequest httpServletRequest);

}
