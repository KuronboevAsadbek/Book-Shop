package uz.bookshop.controller.service_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.bookshop.service.OrderDetailsService;

import static uz.bookshop.utils.Endpoint.GET_ALL;
import static uz.bookshop.utils.Endpoint.ORDER_DETAILS;

@RestController
@RequiredArgsConstructor
@RequestMapping(ORDER_DETAILS)
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;


    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @GetMapping(GET_ALL)
    public ResponseEntity<?> getAllOrderDetails(HttpServletRequest httpServletRequest,
                                                @RequestParam("orderId") Long orderId) {
        return ResponseEntity.ok(orderDetailsService.getAllOrderDetails(httpServletRequest, orderId));
    }
}
