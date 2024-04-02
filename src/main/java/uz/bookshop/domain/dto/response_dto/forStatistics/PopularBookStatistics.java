package uz.bookshop.domain.dto.response_dto.forStatistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bookshop.domain.model.Book;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopularBookStatistics {

    private List<Book> popularBooks;
}
