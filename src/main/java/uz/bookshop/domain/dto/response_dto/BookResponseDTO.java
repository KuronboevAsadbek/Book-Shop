package uz.bookshop.domain.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO implements Serializable {

    private Long id;
    private String name;
    private Integer price;
    private String authorName;
    private String authorSurname;
}
