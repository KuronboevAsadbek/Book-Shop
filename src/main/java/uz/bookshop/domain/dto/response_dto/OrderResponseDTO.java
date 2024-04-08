package uz.bookshop.domain.dto.response_dto;

import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    @Id
    private Long id;
//    private Long userId;
    private Integer totalAmount;
    private Boolean status;
    private List<OrderDetailsResponseDTO> orderDetails;
}
