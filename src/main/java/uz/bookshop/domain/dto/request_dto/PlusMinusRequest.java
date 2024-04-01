package uz.bookshop.domain.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlusMinusRequest {


    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("plus_or_minus")
    private int plusOrMinus;
}
