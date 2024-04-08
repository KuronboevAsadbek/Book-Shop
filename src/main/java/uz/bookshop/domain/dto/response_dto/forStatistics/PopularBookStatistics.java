package uz.bookshop.domain.dto.response_dto.forStatistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopularBookStatistics {

    private String bookName;
    private Long totalQuantity;
}
