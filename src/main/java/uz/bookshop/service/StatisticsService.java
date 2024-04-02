package uz.bookshop.service;

import uz.bookshop.domain.dto.response_dto.forStatistics.UserStatisticsResponse;

public interface StatisticsService {

    UserStatisticsResponse lastWeekRegisteredUsers();

    UserStatisticsResponse userStatisticsByPeriod(String startDate, String endDate);
}
