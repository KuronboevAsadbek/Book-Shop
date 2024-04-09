package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.model.Book;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByName(String name);

    List<Book> findByCreatedBy(String userName);

}
