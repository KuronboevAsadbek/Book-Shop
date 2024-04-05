package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.bookshop.domain.model.OrderDetails;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {


    @Query(value = "SELECT od FROM order_details od WHERE od.order_id = ?1", nativeQuery = true)
    List<OrderDetails> getOrderDetailsByOrderId(Long orderId);


    @Query(value = "SELECT * FROM order_details WHERE order_id = ?1", nativeQuery = true)
    List<OrderDetails> findByOrderId(Long id);
}