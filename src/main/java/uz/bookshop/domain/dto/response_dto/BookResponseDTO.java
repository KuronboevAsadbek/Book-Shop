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
public class BookResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6159442722173951879L;
    private Long id;
    private String name;
    private Integer price;
    private Integer quantity;
    private String authorName;
    private String authorSurname;
}
