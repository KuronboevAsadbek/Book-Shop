package uz.bookshop.domain.audit;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt", "createdBy"},
        allowGetters = true
)
public abstract class DateAudit implements Serializable {
    @CreatedDate
    @Column(updatable = false)
    @CurrentTimestamp
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}
