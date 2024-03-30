package uz.bookshop.domain.dto.request_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {

    private Long bookId;

    private Integer quantity;

}
