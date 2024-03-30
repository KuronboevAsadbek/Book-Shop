package uz.bookshop.domain.dto.request_dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("price")
    private BigInteger price;

    @NotNull
    @JsonProperty("quantity")
    private Integer quantity;


}
