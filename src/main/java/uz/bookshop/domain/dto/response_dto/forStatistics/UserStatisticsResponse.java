package uz.bookshop.domain.dto.response_dto.forStatistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticsResponse {

    private String username;
    private String firstName;
    private String lastName;
    private Date createdAt;

}
