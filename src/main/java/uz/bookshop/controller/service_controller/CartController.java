package uz.bookshop.controller.service_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.CartRequestDTO;
import uz.bookshop.domain.dto.request_dto.PlusMinusRequest;
import uz.bookshop.domain.dto.response_dto.CartResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.service.CartService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequestMapping(CART)
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @PostMapping(ADD)
    public ResponseEntity<ResponseDTO> addToBasket(@RequestBody CartRequestDTO cartRequestDTO) {
        return ResponseEntity.ok(cartService.addToBasket(cartRequestDTO));
    }

    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @GetMapping(OPEN)
    public ResponseEntity<?> openBasket() {
        return ResponseEntity.ok(cartService.openBasket());

    }

    @DeleteMapping(DELETE_CART)
    public ResponseEntity<ResponseDTO> deleteFromBasket() {
        return ResponseEntity.ok(cartService.deleteCarts());
    }

    @PutMapping(ADD_MINUST_OR_PLUS)
    public ResponseEntity<?> addOneOrMinusOne(@RequestBody PlusMinusRequest plusMinusRequest) {
        cartService.addOneOrMinusOne(plusMinusRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(DELETE_ONE_CART)
    public ResponseEntity<ResponseDTO> deleteOneCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.deleteOneCart(id));
    }

}
