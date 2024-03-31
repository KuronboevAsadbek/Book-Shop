package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.bookshop.domain.model.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.userId = ?1")
    List<Cart> findAllByUserId(Long currentUserId);


    @Modifying
    @Query(value = "DELETE  FROM cart c WHERE c.user_id = ?1", nativeQuery = true)
    void deleteAllCartsByUserId(Long userId);

    @Query(value = "SELECT * FROM cart c WHERE c.user_id = ?1", nativeQuery = true)
    List<Cart> findByUserId(Long userId);
}
