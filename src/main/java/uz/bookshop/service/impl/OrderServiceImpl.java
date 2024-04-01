package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;
import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.model.Order;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.repository.OrderDetailsRepository;
import uz.bookshop.repository.OrderRepository;
import uz.bookshop.service.CartService;
import uz.bookshop.service.OrderDetailsService;
import uz.bookshop.service.OrderService;

import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrderDetailsService orderDetailsService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CartService cartService;

    @Override
    public OrderResponseDTO createOrder() {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setUserId(jwtTokenProvider.getCurrentUserId());
        List<OrderDetailsResponseDTO> detailsResponseDTO = orderDetailsService.addOrderDetails(
                jwtTokenProvider.getCurrentUserId());
        BigInteger total = BigInteger.ZERO;
        for (OrderDetailsResponseDTO orderDetailsResponseDTO : detailsResponseDTO) {
            total = total.add(orderDetailsResponseDTO.getTotalPrice());
        }
        orderResponseDTO.setTotalAmount(total);
        orderResponseDTO.setStatus(true);
        orderResponseDTO.setOrderDetails(detailsResponseDTO);
        Order order = new Order();
        order.setUserId(orderResponseDTO.getUserId());
        order.setTotalAmount(orderResponseDTO.getTotalAmount());
        order.setStatus(true);
        Order  order1 = orderRepository.save(order);
        orderResponseDTO.setId(order1.getId());
        orderDetailsRepository.setOrderId(order1.getId(), jwtTokenProvider.getCurrentUser());
        cartService.deleteCarts();
        return orderResponseDTO;

    }

    @Override
    public ResponseDTO deleteOrder() {
        return null;
    }
}
