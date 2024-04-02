package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.bookshop.config.network.NetworkDataService;
import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;
import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.model.Order;
import uz.bookshop.domain.model.OrderDetails;
import uz.bookshop.exception.CartException;
import uz.bookshop.exception.OrderDetailsException;
import uz.bookshop.exception.OrderException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.OrderDetailsRepository;
import uz.bookshop.repository.OrderRepository;
import uz.bookshop.service.CartService;
import uz.bookshop.service.OrderDetailsService;
import uz.bookshop.service.OrderService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final static Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrderDetailsService orderDetailsService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CartService cartService;
    private final Gson gson;
    private final NetworkDataService networkDataService;


    @Override
    @Transactional
    public OrderResponseDTO createOrder(HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
            orderResponseDTO.setUserId(jwtTokenProvider.getCurrentUserId());
            List<OrderDetailsResponseDTO> detailsResponseDTO = orderDetailsService.addOrderDetails(
                    jwtTokenProvider.getCurrentUserId(), httpServletRequest);
            if (detailsResponseDTO.isEmpty()) {
                throw new OrderException("Order details are empty");
            }
            Integer total = 0;
            for (OrderDetailsResponseDTO orderDetailsResponseDTO : detailsResponseDTO) {
                total = orderDetailsResponseDTO.getTotalPrice()+total;
            }
            orderResponseDTO.setTotalAmount(total);
            orderResponseDTO.setStatus(true);
            orderResponseDTO.setOrderDetails(detailsResponseDTO);
            Order order = new Order();
            order.setUserId(orderResponseDTO.getUserId());
            order.setTotalAmount(orderResponseDTO.getTotalAmount());
            order.setStatus(true);
            Order order1 = orderRepository.save(order);
            orderResponseDTO.setId(order1.getId());
            for (OrderDetailsResponseDTO orderDetailsResponseDTO : detailsResponseDTO) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderId(order1.getId());
                orderDetails.setBookId(orderDetailsResponseDTO.getBook().getId());
                orderDetails.setPrice(orderDetailsResponseDTO.getBook().getPrice());
                orderDetails.setQuantity(orderDetailsResponseDTO.getQuantity());
                orderDetailsRepository.save(orderDetails);
            }
            cartService.deleteCarts(httpServletRequest);
            LOG.info("Order Created \t\t {}", gson.toJson(orderResponseDTO));
            return orderResponseDTO;

        } catch (CartException e) {
            throw new CartException(e.getMessage());
        } catch (OrderDetailsException e) {
            throw new OrderDetailsException(e.getMessage());
        } catch (Exception e) {
            LOG.error("Error while creating order", e);
            throw new OrderException("Error while creating order");
        }
    }

    @Override
    public ResponseDTO deleteOrder() {
        return null;
    }


}
