package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;
import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.model.Order;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.repository.OrderRepository;
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

    @Override
    public OrderResponseDTO createOrder() {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setUserId(jwtTokenProvider.getCurrentUserId());
        List<OrderDetailsResponseDTO> detailsResponseDTO = orderDetailsService.addOrderDetails(
                jwtTokenProvider.getCurrentUserId());
        BigInteger total = BigInteger.ZERO;
        for (OrderDetailsResponseDTO orderDetailsResponseDTO : detailsResponseDTO) {
            total = total.add(orderDetailsResponseDTO.getPrice().multiply(BigInteger.valueOf(orderDetailsResponseDTO.getQuantity())));
        }
        orderResponseDTO.setTotalAmount(total);
        orderResponseDTO.setStatus(true);
        orderResponseDTO.setOrderDetails(detailsResponseDTO);
        Order order = new Order();
        order.setUserId(orderResponseDTO.getUserId());
        order.setTotalAmount(orderResponseDTO.getTotalAmount());
        order.setOrderDetails(orderDetailsService.getAllOrderDetails());
        order.setStatus(true);
        orderRepository.save(order);
        return orderResponseDTO;

    }

    @Override
    public ResponseDTO deleteOrder() {
        return null;
    }
}
