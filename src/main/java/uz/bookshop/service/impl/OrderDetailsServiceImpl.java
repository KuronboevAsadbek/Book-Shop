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
import uz.bookshop.mapping.BookMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.CartRepository;
import uz.bookshop.repository.OrderDetailsRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.OrderDetailsService;

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


    @Override
    public List<OrderDetailsResponseDTO> addOrderDetails(Long userId) {
        List<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            throw new CartException("Cart is empty");
        }
        List<OrderDetailsResponseDTO> orderDetailsResponseDTOS = new ArrayList<>();
        BookResponseDTO bookResponseDTO;

        for (Cart cart1 : cart) {

            Book book = bookRepository.findById(cart1.getBookId()).orElseThrow(() ->
                    new BookException("Book not found"));
            bookResponseDTO = bookMapper.toDto(book);
            User bookAuthor = userRepository.findById(book.getUserId()).orElseThrow(() ->
                    new UserException("Author not found"));
//            bookResponseDTO.setAuthorName(bookAuthor.getFirstName());
//            bookResponseDTO.setAuthorSurname(bookAuthor.getLastName());
            OrderDetailsResponseDTO orderDetailsResponseDTO = new OrderDetailsResponseDTO();
            orderDetailsResponseDTO.setBook(bookResponseDTO);
//            orderDetailsResponseDTO.setPrice(book.getPrice());
//            orderDetailsResponseDTO.setQuantity(cart1.getQuantity());
            orderDetailsResponseDTOS.add(orderDetailsResponseDTO);
        }
        for (OrderDetailsResponseDTO orderDetailsResponseDTO : orderDetailsResponseDTOS) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setBookId(orderDetailsResponseDTO.getBook().getId());
            orderDetails.setPrice(orderDetailsResponseDTO.getPrice());
            orderDetails.setQuantity(orderDetailsResponseDTO.getQuantity());
            orderDetailsRepository.save(orderDetails);
        }

        return orderDetailsResponseDTOS;

    }

    @Override
    public void deleteOrderDetails(Long id) {

    }

    @Override
    public List<OrderDetails> getAllOrderDetails() {
        return null;
    }
}
