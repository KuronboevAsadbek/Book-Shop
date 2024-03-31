package uz.bookshop.service;

import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;
import uz.bookshop.domain.model.OrderDetails;

import java.util.List;

public interface OrderDetailsService {

    List<OrderDetailsResponseDTO> addOrderDetails(Long userId);

    void deleteOrderDetails(Long id);

    List<OrderDetails> getAllOrderDetails();


}
