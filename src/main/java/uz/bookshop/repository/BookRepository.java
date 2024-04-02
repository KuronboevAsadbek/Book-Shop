package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.bookshop.domain.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByName(String name);
}
