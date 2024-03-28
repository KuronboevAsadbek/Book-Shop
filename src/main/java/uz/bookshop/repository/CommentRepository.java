package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bookshop.domain.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
