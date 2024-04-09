package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.bookshop.config.network.NetworkDataService;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;
import uz.bookshop.domain.model.Book;
import uz.bookshop.domain.model.Cart;
import uz.bookshop.domain.model.OrderDetails;
import uz.bookshop.exception.BookException;
import uz.bookshop.exception.CartException;
import uz.bookshop.exception.OrderDetailsException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.BookMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.OrderDetailsRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.OrderDetailsService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final static Logger LOG = LoggerFactory.getLogger(OrderDetailsServiceImpl.class);
    private final OrderDetailsRepository orderDetailsRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CartServiceImp cartServiceImp;
    private final Gson gson;
    private final NetworkDataService networkDataService;

    @NotNull
    private static OrderDetailsResponseDTO getOrderDetailsResponseDTO(Cart cart1, Book book,
                                                                      BookResponseDTO bookResponseDTO) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setBookId(book.getId());
        orderDetails.setPrice(book.getPrice());
        orderDetails.setQuantity(cart1.getQuantity());
//            orderDetails = orderDetailsRepository.save(orderDetails);
        OrderDetailsResponseDTO orderDetailsResponseDTO = new OrderDetailsResponseDTO();
        orderDetailsResponseDTO.setId(orderDetails.getId());
        orderDetailsResponseDTO.setBook(bookResponseDTO);
        orderDetailsResponseDTO.setTotalPrice(orderDetails.getPrice() * orderDetails.getQuantity());
//        orderDetailsResponseDTO.setQuantity(orderDetails.getQuantity());
        return orderDetailsResponseDTO;
    }

    @Override
    public List<OrderDetailsResponseDTO> addOrderDetails(Long userId, HttpServletRequest httpServletRequest) {
        try {
            String ClientIP = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            String cartKey = "cart: " + JwtTokenProvider.getCurrentUser();

            List<Cart> cart = cartServiceImp.getAllCarts(cartKey);
            List<OrderDetailsResponseDTO> orderDetailsResponseDTOS = new ArrayList<>();
            for (Cart cart1 : cart) {
                Book book = bookRepository.findById(cart1.getBookId())
                        .orElseThrow(() -> new BookException("Book not found"));
//                User bookAuthor = userRepository.findById(book.getUserId())
//                        .orElseThrow(() -> new UserException("Author not found"));
                BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
                bookResponseDTO.setWriter(book.getWriter());
//                bookResponseDTO.setAuthorSurname(bookAuthor.getLastName());
                OrderDetailsResponseDTO orderDetailsResponseDTO =
                        getOrderDetailsResponseDTO(cart1, book, bookResponseDTO);
                if (cart1.getQuantity() > book.getQuantity())
                    throw new OrderDetailsException("Book quantity is not enough");
                orderDetailsResponseDTOS.add(orderDetailsResponseDTO);

            }
            updateBook(orderDetailsResponseDTOS, httpServletRequest, cart);
            LOG.info("Order Details Created \t\t {}", gson.toJson(orderDetailsResponseDTOS));
            return orderDetailsResponseDTOS;
        } catch (CartException e) {
            throw new CartException(e.getMessage());
        } catch (Exception e) {
            throw new OrderDetailsException("Error in adding order details");
        }
    }

    @Override
    public List<OrderDetailsResponseDTO> getAllOrderDetails(HttpServletRequest httpServletRequest,
                                                            Long orderId) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderId(orderId);
            List<OrderDetailsResponseDTO> orderDetailsResponseDTOS = new ArrayList<>();
//            for (OrderDetails orderDetails1 : orderDetails) {
//                Book book = bookRepository.findById(orderDetails1.getBookId())
//                        .orElseThrow(() -> new BookException("Book not found"));
//                BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
//                bookResponseDTO.setQuantity(orderDetails1.getQuantity());
//                bookResponseDTO.setWriter(book.getWriter());
//                OrderDetailsResponseDTO orderDetailsResponseDTO = new OrderDetailsResponseDTO();
//                orderDetailsResponseDTO.setId(orderDetails1.getId());
//                orderDetailsResponseDTO.setBook(bookResponseDTO);
//                orderDetailsResponseDTO.setTotalPrice(orderDetails1.getPrice() * orderDetails1.getQuantity());
//                orderDetailsResponseDTOS.add(orderDetailsResponseDTO);
//            }
            orderDetails.stream().map(orderDetails1 -> {
                Book book = bookRepository.findById(orderDetails1.getBookId())
                        .orElseThrow(() -> new BookException("Book not found"));
                BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
                bookResponseDTO.setQuantity(orderDetails1.getQuantity());
                bookResponseDTO.setWriter(book.getWriter());
                OrderDetailsResponseDTO orderDetailsResponseDTO = new OrderDetailsResponseDTO();
                orderDetailsResponseDTO.setId(orderDetails1.getId());
                orderDetailsResponseDTO.setBook(bookResponseDTO);
                orderDetailsResponseDTO.setTotalPrice(orderDetails1.getPrice() * orderDetails1.getQuantity());
                return orderDetailsResponseDTO;
            }).forEach(orderDetailsResponseDTOS::add);
            LOG.info("Order Details \t\t {}", gson.toJson(orderDetailsResponseDTOS));
            return orderDetailsResponseDTOS;
        } catch (Exception e) {
            throw new OrderDetailsException("Order details not found");
        }
    }

    @Override
    public OrderDetailsResponseDTO getOrderDetailsById(Long id, HttpServletRequest httpServletRequest) {
        return null;
    }

    public void updateBook(List<OrderDetailsResponseDTO> orderDetailsResponseDTOS,
                           HttpServletRequest httpServletRequest,
                           List<Cart> cart) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));

//            for (OrderDetailsResponseDTO orderDetailsResponseDTO : orderDetailsResponseDTOS) {
//                Book book = bookRepository.findById(orderDetailsResponseDTO.getBook().getId())
//                        .orElseThrow(() -> new BookException("Book not found"));
//                for (Cart cart1 : cart) {
//                    if (cart1.getBookId().equals(book.getId())) {
//                        book.setQuantity(book.getQuantity() - cart1.getQuantity());
//                        bookRepository.save(book);
//                    }
//
//                }
//            }
            orderDetailsResponseDTOS.stream().peek(orderDetailsResponseDTO -> {
                Book book = bookRepository.findById(orderDetailsResponseDTO.getBook().getId())
                        .orElseThrow(() -> new BookException("Book not found"));
                cart.stream().filter(cart1 -> cart1.getBookId().equals(book.getId())).forEach(cart1 -> {
                    book.setQuantity(book.getQuantity() - cart1.getQuantity());
                    bookRepository.save(book);
                });
            }).forEach(orderDetailsResponseDTO -> {
            });

        } catch (Exception e) {
            LOG.error("Error in updating book {}", e.getMessage());
            throw new BookException("Error in updating book");
        }
    }

    public Integer quantity(OrderDetails orderDetails) {

        try {
            List<Cart> cart = cartServiceImp.getAllCarts("cart: " + JwtTokenProvider.getCurrentUser());
//            for (Cart cart1 : cart) {
//                if (cart1.getBookId().equals(orderDetails.getBookId())) {
//                    return cart1.getQuantity();
//                }

//            }
            cart.stream().filter(cart1 -> cart1.getBookId().equals(orderDetails.getBookId())).forEach(Cart::getQuantity);
        } catch (Exception e) {
            LOG.error("Error in getting quantity {}", e.getMessage());
            throw new OrderDetailsException("Error in getting quantity");
        }
        return 0;
    }
}
