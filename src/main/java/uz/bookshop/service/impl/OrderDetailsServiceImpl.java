package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.dto.response_dto.OrderDetailsResponseDTO;
import uz.bookshop.domain.model.Book;
import uz.bookshop.domain.model.Cart;
import uz.bookshop.domain.model.OrderDetails;
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.BookException;
import uz.bookshop.exception.CartException;
import uz.bookshop.exception.UserException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.BookMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.CartRepository;
import uz.bookshop.repository.OrderDetailsRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.OrderDetailsService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CartServiceImp cartServiceImp;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public List<OrderDetailsResponseDTO> addOrderDetails(Long userId) {
        String cartKey = "cart: " + jwtTokenProvider.getCurrentUser();
        List<Cart> cart = cartServiceImp.getAllCarts(cartKey);
        if (cart.isEmpty()) {
            throw new CartException("Cart is empty");
        }
        List<OrderDetailsResponseDTO> orderDetailsResponseDTOS = new ArrayList<>();

        for (Cart cart1 : cart) {
            Book book = bookRepository.findById(cart1.getBookId())
                    .orElseThrow(() -> new BookException("Book not found"));
            User bookAuthor = userRepository.findById(book.getUserId())
                    .orElseThrow(() -> new UserException("Author not found"));

            BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
            bookResponseDTO.setAuthorName(bookAuthor.getFirstName());
            bookResponseDTO.setAuthorSurname(bookAuthor.getLastName());

            OrderDetails orderDetails = new OrderDetails(); // Create a new OrderDetails object for each cart item

            orderDetails.setBookId(book.getId());
            orderDetails.setPrice(book.getPrice());
            orderDetails.setQuantity(cart1.getQuantity());

            orderDetails = orderDetailsRepository.save(orderDetails);

            OrderDetailsResponseDTO orderDetailsResponseDTO = new OrderDetailsResponseDTO();
            orderDetailsResponseDTO.setId(orderDetails.getId());
            orderDetailsResponseDTO.setBook(bookResponseDTO);
            orderDetailsResponseDTO.setTotalPrice(orderDetails.getPrice().multiply(BigInteger.valueOf(orderDetails.getQuantity())));
            orderDetailsResponseDTO.setQuantity(orderDetails.getQuantity());
            orderDetailsResponseDTOS.add(orderDetailsResponseDTO);
        }

        return orderDetailsResponseDTOS;


    }
}
