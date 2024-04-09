package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
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
import uz.bookshop.mapping.OrderMapper;
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
    private final OrderMapper orderMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrderDetailsService orderDetailsService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailsServiceImpl orderDetailsServiceimpl;
    private final CartService cartService;
    private final Gson gson;
    private final NetworkDataService networkDataService;

    @Override
    @Transactional
    public ResponseDTO createOrder(HttpServletRequest httpServletRequest) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            String ClientIP = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
//            orderResponseDTO.setUserId(jwtTokenProvider.getCurrentUserId());
            List<OrderDetailsResponseDTO> detailsResponseDTO = orderDetailsService.addOrderDetails(
                    jwtTokenProvider.getCurrentUserId(), httpServletRequest);
            if (detailsResponseDTO.isEmpty()) {
                throw new OrderException("Order details are empty");
            }
            Integer total = 0;
            for (OrderDetailsResponseDTO orderDetailsResponseDTO : detailsResponseDTO) {
                total = orderDetailsResponseDTO.getTotalPrice() + total;
            }
            orderResponseDTO.setTotalAmount(total);
            orderResponseDTO.setStatus(true);
            orderResponseDTO.setOrderDetails(detailsResponseDTO);
            Order order = new Order();
//            order.setUserId(orderResponseDTO.getUserId());
            order.setTotalAmount(orderResponseDTO.getTotalAmount());
            order.setStatus(true);
            Order order1 = orderRepository.save(order);


            orderResponseDTO.setId(order1.getId());
            for (OrderDetailsResponseDTO orderDetailsResponseDTO : detailsResponseDTO) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrder(order1);
                orderDetails.setBookId(orderDetailsResponseDTO.getBook().getId());
                orderDetails.setPrice(orderDetailsResponseDTO.getBook().getPrice());
                orderDetails.setQuantity(orderDetailsServiceimpl.quantity(orderDetails));
                orderDetailsRepository.save(orderDetails);
            }


            cartService.deleteCarts(httpServletRequest);
            LOG.info("Order Created \t\t {}", gson.toJson(orderResponseDTO));
            responseDTO.setMessage("Order created successfully");
            return responseDTO;

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
    public OrderResponseDTO getLastOrder(HttpServletRequest httpServletRequest) {
        try {
            String ClientIP = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            Order order = orderRepository.findFirstByCreatedByOrderByCreatedAtDesc(JwtTokenProvider.getCurrentUser());
            OrderResponseDTO orderResponseDTO = orderMapper.toDto(order);
            List<OrderDetailsResponseDTO> orderDetailsResponseDTOS = orderDetailsService.getAllOrderDetails(
                    httpServletRequest, orderResponseDTO.getId());
            orderResponseDTO.setOrderDetails(orderDetailsResponseDTOS);
            LOG.info("Last Order \t\t {}", gson.toJson(orderResponseDTO));
            return orderResponseDTO;
        } catch (OrderDetailsException e) {
            LOG.error("Error while getting last order", e);
            throw new OrderDetailsException(e.getMessage());
        } catch (Exception e) {
            LOG.error("Error while getting last order", e);
            throw new OrderException("Error while getting last order");
        }
    }

    @Override
    public List<OrderResponseDTO> getAllOrders(HttpServletRequest httpServletRequest) {
        try {
            String ClientIP = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            List<Order> orders = orderRepository.findByUsername(JwtTokenProvider.getCurrentUser());
            List<OrderResponseDTO> orderResponseDTOS = orderMapper.toDto(orders);
            for (OrderResponseDTO orderResponseDTO : orderResponseDTOS) {
                List<OrderDetailsResponseDTO> orderDetailsResponseDTOS = orderDetailsService.getAllOrderDetails(
                        httpServletRequest, orderResponseDTO.getId());
                orderResponseDTO.setOrderDetails(orderDetailsResponseDTOS);

            }
            LOG.info("Orders \t\t {}", gson.toJson(orderResponseDTOS));
            return orderResponseDTOS;
        } catch (OrderDetailsException e) {
            LOG.error("Error while getting orders", e);
            throw new OrderDetailsException(e.getMessage());
        } catch (Exception e) {
            LOG.error("Error while getting orders", e);
            throw new OrderException("Error while getting orders");
        }
    }

}

