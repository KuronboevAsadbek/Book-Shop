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
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.BookException;
import uz.bookshop.exception.CartException;
import uz.bookshop.exception.OrderDetailsException;
import uz.bookshop.exception.UserException;
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
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CartServiceImp cartServiceImp;
    private final JwtTokenProvider jwtTokenProvider;
    private final Gson gson;
    private final NetworkDataService networkDataService;

    @NotNull
    private static OrderDetailsResponseDTO getOrderDetailsResponseDTO(Cart cart1, Book book, BookResponseDTO bookResponseDTO) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setBookId(book.getId());
        orderDetails.setPrice(book.getPrice());
        orderDetails.setQuantity(cart1.getQuantity());
//            orderDetails = orderDetailsRepository.save(orderDetails);
        OrderDetailsResponseDTO orderDetailsResponseDTO = new OrderDetailsResponseDTO();
        orderDetailsResponseDTO.setId(orderDetails.getId());
        orderDetailsResponseDTO.setBook(bookResponseDTO);
        orderDetailsResponseDTO.setTotalPrice(orderDetails.getPrice() * orderDetails.getQuantity());
        orderDetailsResponseDTO.setQuantity(orderDetails.getQuantity());
        return orderDetailsResponseDTO;
    }

    @Override
    public List<OrderDetailsResponseDTO> addOrderDetails(Long userId, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            String cartKey = "cart: " + jwtTokenProvider.getCurrentUser();

            List<Cart> cart = cartServiceImp.getAllCarts(cartKey);
            List<OrderDetailsResponseDTO> orderDetailsResponseDTOS = new ArrayList<>();
            for (Cart cart1 : cart) {
                Book book = bookRepository.findById(cart1.getBookId())
                        .orElseThrow(() -> new BookException("Book not found"));
                User bookAuthor = userRepository.findById(book.getUserId())
                        .orElseThrow(() -> new UserException("Author not found"));
                BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
                bookResponseDTO.setAuthorName(bookAuthor.getFirstName());
                bookResponseDTO.setAuthorSurname(bookAuthor.getLastName());
                OrderDetailsResponseDTO orderDetailsResponseDTO = getOrderDetailsResponseDTO(cart1, book, bookResponseDTO);
                orderDetailsResponseDTOS.add(orderDetailsResponseDTO);

            }
            UpdateBook(orderDetailsResponseDTOS, httpServletRequest);
            LOG.info("Order Details Created \t\t {}", gson.toJson(orderDetailsResponseDTOS));
            return orderDetailsResponseDTOS;
        } catch (CartException e) {
            throw new CartException(e.getMessage());
        } catch (Exception e) {
            throw new OrderDetailsException("Cart is empty");
        }
    }

    public void UpdateBook(List<OrderDetailsResponseDTO> orderDetailsResponseDTOS, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));

            for (OrderDetailsResponseDTO orderDetailsResponseDTO : orderDetailsResponseDTOS) {
                Book book = bookRepository.findById(orderDetailsResponseDTO.getBook().getId())
                        .orElseThrow(() -> new BookException("Book not found"));
                book.setQuantity(book.getQuantity() - orderDetailsResponseDTO.getQuantity());
                LOG.info("Book Updated");
                bookRepository.save(book);
            }
        } catch (Exception e) {
            LOG.error("Error in updating book {}", e.getMessage());
            throw new BookException("Error in updating book");
        }
    }
}
