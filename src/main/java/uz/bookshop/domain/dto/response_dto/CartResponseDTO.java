package uz.bookshop.domain.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    private Long id;
    private Long bookId;
    private String authorName;
    private Integer quantity;
    private Integer amount;
    private Integer totalAmount;
    private Integer available;


}
