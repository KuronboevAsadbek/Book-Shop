package uz.bookshop.domain.dto.request_dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8964724010101867347L;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("price")
    private Integer price;

    @NotNull
    @JsonProperty("quantity")
    private Integer quantity;

    @NotNull
    @JsonProperty("writer")
    private String writer;


}
