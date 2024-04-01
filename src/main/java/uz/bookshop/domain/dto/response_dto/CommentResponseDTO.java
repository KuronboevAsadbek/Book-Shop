package uz.bookshop.domain.dto.response_dto;

import jakarta.persistence.Id;
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

    @Id
    private Long id;
    private String text;
    private BookResponseDTO book;
    private String createdAt;
    private String createdBy;
}