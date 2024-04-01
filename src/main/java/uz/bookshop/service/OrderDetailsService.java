package uz.bookshop.service;

import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;

import java.util.List;

public interface OrderDetailsService {

    List<OrderDetailsResponseDTO> addOrderDetails(Long userId);




}
