package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.bookshop.domain.model.OrderDetails;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {


    @Query(value = "SELECT o FROM order_details o WHERE o.created_by=:username", nativeQuery = true)
    List<OrderDetails> getAllByUserName(String username);
//
//
    @Modifying
    @Transactional
    @Query(value = "UPDATE order_details SET order_id=:orderId WHERE order_details.created_by=:username", nativeQuery = true)
    void setOrderId(Long orderId, String username);
}