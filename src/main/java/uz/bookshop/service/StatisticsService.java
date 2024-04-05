package uz.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
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

    List<BooksStatisticsResponse> booksStatistics();

    List<ActiveUserStatistics> activeUsersStatistics(HttpServletRequest httpServletRequest);


}
