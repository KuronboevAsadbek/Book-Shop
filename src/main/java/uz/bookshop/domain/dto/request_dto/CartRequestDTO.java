package uz.bookshop.domain.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {

    @NotNull
    @JsonProperty("book_id")
    private Long bookId;

    @NotNull
    @JsonProperty("quantity")
    private Integer quantity;

}
