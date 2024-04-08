package uz.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;
import uz.bookshop.domain.dto.response_dto.forStatistics.ActiveUserStatistics;
import uz.bookshop.domain.dto.response_dto.forStatistics.BooksStatisticsResponse;
import uz.bookshop.domain.dto.response_dto.forStatistics.PopularBookStatistics;
import uz.bookshop.domain.dto.response_dto.forStatistics.UserStatisticsResponse;

import java.util.List;

public interface StatisticsService {

    List<UserStatisticsResponse> lastWeekRegisteredUsers(HttpServletRequest httpServletRequest);

    List<UserStatisticsResponse> userStatisticsByPeriod(String startDate, String endDate,
                                                        HttpServletRequest httpServletRequest);

    List<PopularBookStatistics> popularBooksStatistics(HttpServletRequest httpServletRequest);

    List<BooksStatisticsResponse> booksStatistics(HttpServletRequest httpServletRequest);

    List<ActiveUserStatistics> activeUsersStatistics(HttpServletRequest httpServletRequest, MultiValueMap<String, String> queryParams);


}
