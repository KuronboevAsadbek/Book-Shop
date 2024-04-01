package uz.bookshop.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.bookshop.domain.audit.DateAudit;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Cart extends DateAudit implements Serializable {

    @Serial
    private static final long serialVersionUID = -1949685906633907081L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quantity")
    private Integer quantity;


}