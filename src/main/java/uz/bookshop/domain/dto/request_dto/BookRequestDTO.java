package uz.bookshop.domain.dto.request_dto;


import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private BigInteger price;
}
