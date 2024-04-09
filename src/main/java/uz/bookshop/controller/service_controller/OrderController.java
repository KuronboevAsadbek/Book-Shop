package uz.bookshop.controller.service_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.service.OrderService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequestMapping(ORDER)
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @PostMapping(CREATE)
    public ResponseEntity<ResponseDTO> createOrder(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(orderService.createOrder(httpServletRequest));
    }

    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @GetMapping(GET_ALL)
    public ResponseEntity<?> getAllOrders(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(orderService.getAllOrders(httpServletRequest));
    }

    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @GetMapping(GET_LAST)
    public ResponseEntity<?> getLastOrder(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(orderService.getLastOrder(httpServletRequest));
    }


}

