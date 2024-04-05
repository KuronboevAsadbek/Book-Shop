package uz.bookshop.domain.dto.response_dto;

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
public class CommentResponseDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 7274043871477772215L;


    private Long id;
    private String text;
    private Long bookId;
    private String bookName;
    private String createdAt;
    private String createdBy;
}
