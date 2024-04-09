package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.bookshop.domain.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.createdBy = :createdBy")
    List<Order> findByUsername(String createdBy);


    Order findFirstByCreatedByOrderByCreatedAtDesc(String createdBy);
}
