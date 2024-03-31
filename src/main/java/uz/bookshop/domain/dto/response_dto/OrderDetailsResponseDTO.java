package uz.bookshop.domain.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponseDTO {

    private Long id;
    private BookResponseDTO book;
    private Integer quantity;
    private BigInteger price;
}
