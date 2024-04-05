package uz.bookshop.domain.dto.response_dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsResponseDTO {

    private Long id;
    private BookResponseDTO book;
    //    private Integer quantity;
    private Integer totalPrice;
}
