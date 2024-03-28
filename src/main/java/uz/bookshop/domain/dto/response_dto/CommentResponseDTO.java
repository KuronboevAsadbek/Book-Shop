package uz.bookshop.domain.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {


    private Long id;
    private String text;
    private BookResponseDTO book;
    private String createdAt;
    private String createdBy;
}
