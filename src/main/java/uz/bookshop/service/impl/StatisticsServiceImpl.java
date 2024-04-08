package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.bookshop.config.network.NetworkDataService;
import uz.bookshop.domain.dto.response_dto.forStatistics.ActiveUserStatistics;
import uz.bookshop.domain.dto.response_dto.forStatistics.BooksStatisticsResponse;
import uz.bookshop.domain.dto.response_dto.forStatistics.PopularBookStatistics;
import uz.bookshop.domain.dto.response_dto.forStatistics.UserStatisticsResponse;
import uz.bookshop.exception.UserStatisticsResponseException;
import uz.bookshop.service.StatisticsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {


    private final static Logger LOG = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    private final EntityManager entityManager;
    private final Gson gson;
    private final NetworkDataService networkDataService;

    @Override
    public List<UserStatisticsResponse> lastWeekRegisteredUsers(HttpServletRequest httpServletRequest) {
        try {
            String clientIP = networkDataService.getClientIPv4Address(httpServletRequest);
            String clientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host: \t\t {}", gson.toJson(clientInfo));
            LOG.info("Client IP: \t\t {}", gson.toJson(clientIP));
            String sql = """
                    SELECT
                        u.username AS username,
                        u.first_name AS firstName,
                        u.last_name AS lastName,
                        CAST(u.created_at AS DATE) AS createdAt
                    FROM
                        users u
                    WHERE
                        CAST(u.created_at AS DATE) BETWEEN CURRENT_DATE - INTERVAL '7 days' AND CURRENT_DATE;
                    """;

            Query query = entityManager.createNativeQuery(sql, UserStatisticsResponse.class);
            List<UserStatisticsResponse> result = query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.error("Error in lastWeekRegisteredUsers: {}", e.getMessage());
            throw new UserStatisticsResponseException("Error in lastWeekRegisteredUsers: " + e.getMessage());
        }
    }

    @Override
    public List<UserStatisticsResponse> userStatisticsByPeriod(String startDate, String endDate,
                                                               HttpServletRequest httpServletRequest) {
        try {
            String clientIP = networkDataService.getClientIPv4Address(httpServletRequest);
            String clientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host: \t\t {}", gson.toJson(clientInfo));
            LOG.info("Client IP: \t\t {}", gson.toJson(clientIP));

            // Parse startDate and endDate strings to Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date parsedStartDate = dateFormat.parse(startDate);
            Date parsedEndDate = dateFormat.parse(endDate);

            String sql = """
                    SELECT
                        u.username AS username,
                        u.first_name AS firstName,
                        u.last_name AS lastName,
                        CAST(u.created_at AS DATE) AS createdAt
                    FROM
                        users u
                    WHERE
                        CAST(u.created_at AS DATE) BETWEEN :startDate AND :endDate
                    """;

            Query query = entityManager.createNativeQuery(sql, UserStatisticsResponse.class);
            query.setParameter("startDate", parsedStartDate);
            query.setParameter("endDate", parsedEndDate);
            List<UserStatisticsResponse> result = query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.error("Error in userStatisticsByPeriod: {}", e.getMessage());
            throw new UserStatisticsResponseException("Error in userStatisticsByPeriod: " + e.getMessage());
        }
    }

    @Override
    public List<PopularBookStatistics> popularBooksStatistics(HttpServletRequest httpServletRequest) {
        try {
            String sql = """
                    SELECT  b.name AS bookName,
                            SUM(o.quantity) AS totalQuantity
                    FROM book b
                    JOIN order_details o ON b.id = o.book_id
                    GROUP BY b.name
                    ORDER BY SUM(o.quantity) DESC
                    LIMIT 5
                    """;

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> rows = query.getResultList();

            List<PopularBookStatistics> result = new ArrayList<>();
            for (Object[] row : rows) {
                PopularBookStatistics stats = new PopularBookStatistics();
                stats.setBookName((String) row[0]);
                stats.setTotalQuantity(((Number) row[1]).intValue());
                result.add(stats);
            }

            return result;
        } catch (Exception e) {
            LOG.error("Error in popularBooksStatistics: {}", e.getMessage());
            throw new UserStatisticsResponseException("Error in popularBooksStatistics: " + e.getMessage());
        }
    }

    @Override
    public List<BooksStatisticsResponse> booksStatistics() {
        try {
            String sql = ("""
                    SELECT
                             b.name                         AS book_name,
                            sum(od.quantity)                AS orders_count,
                            COALESCE(comments_count, 0)     AS comments_count
                    FROM book b
                    JOIN
                            order_details od ON b.id = od.book_id
                    LEFT JOIN
                    (SELECT book_id, COUNT(*) AS comments_count FROM comment GROUP BY book_id) AS c ON b.id = c.book_id
                    GROUP BY b.id, b.name, comments_count
                    ORDER BY orders_count DESC
                    """);
            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> rows = query.getResultList();

            List<BooksStatisticsResponse> result = new ArrayList<>();
            for (Object[] row : rows) {
                BooksStatisticsResponse response = new BooksStatisticsResponse();
                response.setBookName((String) row[0]);
                response.setOrderCount(((Number) row[1]).intValue());
                response.setCommentCount(((Number) row[2]).intValue());
                result.add(response);
            }

            return result;

        } catch (Exception e) {
            LOG.error("Error in booksStatistics: {}", e.getMessage());
            throw new UserStatisticsResponseException("Error in booksStatistics: " + e.getMessage());
        }
    }

    @Override
    public List<ActiveUserStatistics> activeUsersStatistics(HttpServletRequest httpServletRequest, MultiValueMap<String, String> queryParams) {
        try {
            String clientIP = networkDataService.getClientIPv4Address(httpServletRequest);
            String clientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host: \t\t {}", gson.toJson(clientInfo));
            LOG.info("Client IP: \t\t {}", gson.toJson(clientIP));

            String sql = ("""
                        WITH orders AS (
                        SELECT
                            od.created_by,
                            SUM(od.quantity) AS bookCount,
                            SUM(od.price * od.quantity) AS totalAmount
                        FROM
                            order_details od
                        GROUP BY
                            od.created_by
                    ),
                    comments AS (
                        SELECT
                            c.created_by,
                            COUNT(*) AS comment_count
                        FROM
                            comment c
                        GROUP BY
                            c.created_by
                    )
                    SELECT
                        u.id AS id,
                        u.first_name AS name,
                        COALESCE(orders.bookCount, 0) AS bookCount,
                        COALESCE(orders.totalAmount, 0) AS totalAmount,
                        COALESCE(comments.comment_count, 0) AS commentCount
                    FROM
                        users u
                    LEFT JOIN orders ON u.username = orders.created_by
                    LEFT JOIN comments ON u.username = comments.created_by
                    """);
            sql = sortQuery(sql, queryParams);
            Query query = entityManager.createNativeQuery(sql, ActiveUserStatistics.class);
            List<ActiveUserStatistics> resultList = query.getResultList();
            return resultList;

        } catch (Exception e) {
            LOG.error("Error in activeUsersStatistics: {}", e.getMessage());
            throw new UserStatisticsResponseException("Error in activeUsersStatistics: " + e.getMessage());
        }
    }

    private String sortQuery(String sql, MultiValueMap<String, String> queryParams) {
        if (queryParams.containsKey("sort")) {
            String sort = queryParams.getFirst("sort");
            assert sort != null;
            switch (sort) {
                case "price" -> sql += " ORDER BY totalAmount DESC";
                case "bookCount" -> sql += " ORDER BY bookCount DESC";
                case "commentCount" -> sql += " ORDER BY commentCount DESC";
                case "id" -> sql += " ORDER BY id DESC";
            }
        }
        return sql;
    }
}
