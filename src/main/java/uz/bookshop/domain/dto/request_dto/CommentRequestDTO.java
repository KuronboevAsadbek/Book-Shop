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
public class CommentRequestDTO {


    @JsonProperty("text")
    private String text;

    @JsonProperty("book_id")
    private Long bookId;
}
