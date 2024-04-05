package uz.bookshop.domain.audit;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt", "createdBy"},
        allowGetters = true
)
public abstract class DateAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = -5680544693532110285L;
    @CreatedDate
    @Column(updatable = false)

    @CurrentTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tashkent")
    private String createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private String updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}
