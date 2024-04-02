package uz.bookshop.controller.service_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.request_dto.PlusMinusRequest;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.dto.response_dto.Total;
import uz.bookshop.service.CartService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequestMapping(CART)
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @PostMapping(ADD)
    public ResponseEntity<ResponseDTO> addToBasket(@RequestBody CartRequestDTO cartRequestDTO,
                                                   HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cartService.addToBasket(cartRequestDTO, httpServletRequest));
    }

    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @GetMapping(OPEN)
    public ResponseEntity<Total> openBasket(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cartService.openBasket(httpServletRequest));

    }

    @DeleteMapping(DELETE_CART)
    public ResponseEntity<ResponseDTO> deleteFromBasket(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cartService.deleteCarts(httpServletRequest));
    }

    @PutMapping(ADD_MINUS_OR_PLUS)
    public ResponseEntity<?> addOneOrMinusOne(@RequestBody PlusMinusRequest plusMinusRequest,
                                              HttpServletRequest httpServletRequest) {
        cartService.addOneOrMinusOne(plusMinusRequest, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(DELETE_ONE_CART)
    public ResponseEntity<ResponseDTO> deleteOneCart(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cartService.deleteOneCart(id, httpServletRequest));
    }

}
