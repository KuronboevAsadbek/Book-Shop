package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.bookshop.config.network.NetworkDataService;
import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.request_dto.PlusMinusRequest;
import uz.bookshop.domain.dto.response_dto.CartResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.dto.response_dto.Total;
import uz.bookshop.domain.model.Book;
import uz.bookshop.domain.model.Cart;
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.CartException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.CartService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImp implements CartService {


    private final static Logger LOG = LoggerFactory.getLogger(CartServiceImp.class);
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, List<Cart>> redisTemplate;
    private final Gson gson;
    private final NetworkDataService networkDataService;


    @Override
    public ResponseDTO addToBasket(CartRequestDTO cartRequestDTO, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);

            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            ResponseDTO responseDTO = new ResponseDTO();
            Long bookId = cartRequestDTO.getBookId();
            int quantityToAdd = cartRequestDTO.getQuantity();


            Book book = bookRepository.findById(bookId).orElseThrow(() ->
                    new CartException("Book not found"));

            String cartKey = "cart: " + jwtTokenProvider.getCurrentUser();

            // Retrieve the list of items in the cart from Redis
            List<Cart> inCart = getAllCarts(cartKey);
            if (inCart == null) {
                inCart = new ArrayList<>();
            }
            // Check if the book is already in the cart
            Optional<Cart> existingCartItem = inCart.stream()
                    .filter(cart -> cart.getBookId().equals(bookId))
                    .findFirst();
            if (existingCartItem.isPresent()) {
                // If the book is already in the cart, update the quantity
                Cart cart = existingCartItem.get();
                int newQuantity = cart.getQuantity() + quantityToAdd;

                if (newQuantity > book.getQuantity()) {
                    LOG.error("Not enough books in stock");
                    throw new CartException("Not enough books in stock");
                }
                // Update the cart item with the new quantity
                cart.setQuantity(newQuantity);
            } else {
                // If the book is not already in the cart, add it
                if (quantityToAdd > book.getQuantity()) {
                    LOG.error("Not enough books in stock");
                    throw new CartException("Not enough books in stock");
                }

                Cart newCartItem = new Cart();
                newCartItem.setBookId(bookId);
                newCartItem.setUserId(jwtTokenProvider.getCurrentUserId());
                newCartItem.setQuantity(quantityToAdd);

                inCart.add(newCartItem);

            }
            redisTemplate.opsForValue().set(cartKey, inCart);
            LOG.info("Added To Cart  'to Redis' \t\t {}", gson.toJson(cartRequestDTO));
            responseDTO.setMessage("Added to cart");
            return responseDTO;
        } catch (Exception e) {
            LOG.error("Error while adding to cart{}", e.getMessage());
            throw new CartException("Error getting all carts");
        }
    }

    @Override

    public Total openBasket(HttpServletRequest httpServletRequest) {
        try {
            Total total = new Total();
            Integer totalAmountAllBooks = 0;
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            List<CartResponseDTO> cartResponseDTO = new ArrayList<>();
            String username = jwtTokenProvider.getCurrentUser();
            String cartKey = "cart: " + username;
            List<Cart> carts = redisTemplate.opsForValue().get(cartKey);
            assert carts != null;
            for (Cart cart : carts) {
                Book book = bookRepository.findById(cart.getBookId()).orElseThrow(() ->
                        new CartException("Book not found"));
                User user = userRepository.findById(book.getUserId()).orElseThrow(() ->
                        new CartException("Author not found"));
                CartResponseDTO cartResponseDTO1 = getCartResponseDTO(cart, user, book);
                cartResponseDTO.add(cartResponseDTO1);
                totalAmountAllBooks += cartResponseDTO1.getTotalAmount();
                total.setTotalAmountAllBooks(totalAmountAllBooks);
                total.setCarts(cartResponseDTO);
            }
            LOG.info("Cart opened \t\t {}", gson.toJson(cartResponseDTO));
            return total;

        } catch (Exception e) {
            LOG.error("Error when opening cart{}", e.getMessage());
            throw new CartException("Error when opening cart");
        }
    }

    @Override
    public void addOneOrMinusOne(PlusMinusRequest plusMinusRequest, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            Long bookId = plusMinusRequest.getBookId();
            Book book = bookRepository.findById(bookId).orElseThrow(() ->
                    new CartException("Book not found"));
            int plusOrMinus = plusMinusRequest.getPlusOrMinus();
            String username = jwtTokenProvider.getCurrentUser();
            String cartKey = "cart: " + username;
            List<Cart> carts = getAllCarts(cartKey);
            assert carts != null;
            Optional<Cart> cart = carts.stream().filter(cart1 -> cart1.getBookId().equals(bookId)).findFirst();
            if (cart.isPresent()) {
                Cart cart1 = cart.get();
                if (plusOrMinus > 0 && cart1.getQuantity() < book.getQuantity()) {
                    cart1.setQuantity(cart1.getQuantity() + 1);
                } else if (plusOrMinus < 0 && cart1.getQuantity() > 1) {
                    cart1.setQuantity(cart1.getQuantity() - 1);
                }
                redisTemplate.opsForValue().set(cartKey, carts);
                LOG.info("Quantity updated \t\t {}", gson.toJson("to 1"));
            } else {
                LOG.error("Book not found in cart");
                throw new CartException("Book not found in cart");
            }
        } catch (Exception e) {
            LOG.error("Error while updating quantity{}", e.getMessage());
            throw new CartException("Error while updating quantity");

        }

    }

    @Override
    @Transactional
    public ResponseDTO deleteCarts(HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            String username = jwtTokenProvider.getCurrentUser();
            String cartKey = "cart: " + username;
            redisTemplate.delete(cartKey);
            LOG.info("Cart Deleted");
            return new ResponseDTO("Cart deleted");
        } catch (Exception e) {
            log.error("Error while deleting cart{}", e.getMessage());
            throw new CartException("Error while deleting cart");
        }
    }

    @Override
    public ResponseDTO deleteOneCart(Long id, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            String username = jwtTokenProvider.getCurrentUser();
            String cartKey = "cart: " + username;
            List<Cart> carts = getAllCarts(cartKey);
            assert carts != null;
            carts.removeIf(cart -> cart.getBookId().equals(id));
            redisTemplate.opsForValue().set(cartKey, carts);
            LOG.info("Cart item deleted");
            return new ResponseDTO("Cart item deleted");

        } catch (Exception e) {
            LOG.error("Error while deleting cart item{}", e.getMessage());
            throw new CartException("Error while deleting cart item");
        }
    }

    public List<Cart> getAllCarts(String cartKey) {
        try {
            LOG.info("Getting all carts");
            return redisTemplate.opsForValue().get(cartKey);
        } catch (Exception e) {
            LOG.error("Error while getting all carts{}", e.getMessage());
            throw new CartException("Error while getting all carts");
        }

    }

    private static CartResponseDTO getCartResponseDTO(Cart cart, User user, Book book) {
        Total total = new Total();
        CartResponseDTO cartResponseDTO1 = new CartResponseDTO();
        cartResponseDTO1.setAuthorName(user.getFirstName() + " " + user.getLastName());
        cartResponseDTO1.setBookId(cart.getBookId());
        cartResponseDTO1.setQuantity(cart.getQuantity());
        cartResponseDTO1.setTotalAmount(cart.getQuantity() * book.getPrice());
        cartResponseDTO1.setAmount(book.getPrice());
        cartResponseDTO1.setAvailable(book.getQuantity() - cart.getQuantity());
        total.setTotalAmountAllBooks(cart.getQuantity() * book.getPrice());
        return cartResponseDTO1;
    }
}
