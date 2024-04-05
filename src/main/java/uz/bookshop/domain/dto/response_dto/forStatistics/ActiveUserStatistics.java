package uz.bookshop.domain.dto.response_dto.forStatistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActiveUserStatistics {

    private String name;
    private int bookCount;
    private Integer totalAmount;
    private int commentCount;
}
