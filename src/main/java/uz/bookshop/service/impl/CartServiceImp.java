package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.response_dto.CartResponseDTO;
import uz.bookshop.domain.dto.response_dto.DeleteResponse;
import uz.bookshop.domain.model.Book;
import uz.bookshop.domain.model.Cart;
import uz.bookshop.exception.CartException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.CartMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.CartRepository;
import uz.bookshop.service.CartService;

import java.util.List;

import static uz.bookshop.jwt_utils.JwtTokenProvider.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartMapper cartMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public CartResponseDTO addToBasket(CartRequestDTO cartRequestDTO) {
        try {
            Book book = bookRepository.findById(cartRequestDTO.getBookId()).orElseThrow(() ->
                    new CartException("Book not found"));
            if (book.getQuantity() >= cartRequestDTO.getQuantity()) {
                String username = getCurrentUser();
                CartResponseDTO cartResponseDTO = new CartResponseDTO();
                Cart cart = cartMapper.toEntity(cartRequestDTO);
                cart.setUserId(jwtTokenProvider.getCurrentUserId());
                cartRepository.save(cart);
                redisTemplate.delete("cart: " + username);
                cartResponseDTO.setMessage("Added to basket");
                log.info("Added to basket");
                return cartResponseDTO;
            } else {
                log.error("Not enough quantity");
                throw new CartException("Not enough quantity");
            }
        } catch (Exception e) {
            log.error("Error while adding to basket{}", e.getMessage());
            throw new CartException("Error while adding to basket");
        }
    }

    @Override
    public List<CartResponseDTO> openBasket() {
        String username = getCurrentUser();
        String cartKey = "cart: " + username;
        List<Cart> carts = (List<Cart>) redisTemplate.opsForValue().get(cartKey);
        if (carts == null) {
            carts = cartRepository.findAllByUserId(jwtTokenProvider.getCurrentUserId());
            redisTemplate.opsForValue().set(cartKey, carts);
            if (carts.isEmpty()) {
                throw new CartException("Cart is empty");
            }
        }
        return cartMapper.toDto(carts);

    }


    @Override
    public DeleteResponse deleteCarts() {
      try {  String username = getCurrentUser();
        String cartKey = "cart: " + username;
        redisTemplate.delete(cartKey);
        return new DeleteResponse("Cart deleted");
    }catch (Exception e){
        log.error("Error while deleting cart{}", e.getMessage());
        throw new CartException("Error while deleting cart");
    }
}

@Override
public DeleteResponse deleteOneCart(Long id) {
    return null;
}
}
