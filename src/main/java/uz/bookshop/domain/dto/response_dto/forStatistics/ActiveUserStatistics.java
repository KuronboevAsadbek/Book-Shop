package uz.bookshop.domain.dto.response_dto.forStatistics;

import jakarta.persistence.Id;
import lombok.*;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveUserStatistics {

    @Id
    private Long id;

    private String name;

    private Long bookCount;

    private Long totalAmount;

    private Long commentCount;
}
