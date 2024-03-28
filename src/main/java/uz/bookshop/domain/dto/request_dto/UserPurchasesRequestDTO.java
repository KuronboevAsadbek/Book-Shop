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
public class  UserPurchasesRequestDTO {


    @JsonProperty("book_id")
    private Long bookId;

}
