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
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderDetails extends DateAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 2074254117720254871L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;


    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigInteger price;


}
