package uz.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.bookshop.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String userName);

    boolean existsByUsername(String userName);

    @Query(value = "SELECT u FROM User u where u.refreshToken = :refreshToken")
    User findByToken(@Param("refreshToken") String refreshToken);
}
