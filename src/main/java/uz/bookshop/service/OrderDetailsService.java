package uz.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;

import java.util.List;

public interface OrderDetailsService {

    List<OrderDetailsResponseDTO> addOrderDetails(Long userId, HttpServletRequest httpServletRequest);

    List<OrderDetailsResponseDTO> getAllOrderDetails(HttpServletRequest httpServletRequest, Long orderId);

    OrderDetailsResponseDTO getOrderDetailsById(Long id, HttpServletRequest httpServletRequest);


}
