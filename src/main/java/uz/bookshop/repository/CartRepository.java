package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.bookshop.domain.model.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c where c.userId = ?1")
    List<Cart> findAllByUserId(Long currentUserId);
}
