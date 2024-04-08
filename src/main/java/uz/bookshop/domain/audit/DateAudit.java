package uz.bookshop.domain.audit;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt", "createdBy"},
        allowGetters = true
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public abstract class DateAudit {
    @CreatedDate
    @Column(updatable = false)

    @CurrentTimestamp
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Timestamp updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}
