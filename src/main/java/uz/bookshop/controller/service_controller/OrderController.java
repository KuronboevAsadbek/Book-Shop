package uz.bookshop.controller.service_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.service.OrderService;

import static uz.bookshop.utils.Endpoint.CREATE;
import static uz.bookshop.utils.Endpoint.ORDER;

@RestController
@RequestMapping(ORDER)
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(CREATE)
    public ResponseEntity<OrderResponseDTO> createOrder(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(orderService.createOrder(httpServletRequest));
    }
}

