package uz.bookshop.controller.service_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.bookshop.service.StatisticsService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(STATISTICS)
public class StatisticsController {

    private final StatisticsService service;


    @PreAuthorize("hasAuthority('MANAGER_ACCESS')")
    @GetMapping(REGISTERED_USERS)
    public ResponseEntity<?> getLastWeekRegisteredUsers(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.lastWeekRegisteredUsers(httpServletRequest));
    }

    @PreAuthorize("hasAuthority('MANAGER_ACCESS')")
    @GetMapping(GET_USERS_BY_BEGIN_DATE_AND_END_DATE)
    public ResponseEntity<?> getUserStatisticsByPeriod(@RequestParam("start") String startDate,
                                                       @RequestParam("end") String endDate,
                                                       HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.userStatisticsByPeriod(startDate, endDate, httpServletRequest));
    }

    @PreAuthorize("hasAuthority('MANAGER_ACCESS')")
    @GetMapping(POPULAR_BOOKS)
    public ResponseEntity<?> getPopularBooks(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.popularBooksStatistics(httpServletRequest));
    }

    @PreAuthorize("hasAuthority('MANAGER_ACCESS')")
    @GetMapping(BOOK)
    public ResponseEntity<?> getBooksStatistics() {
        return ResponseEntity.ok(service.booksStatistics());
    }

    @PreAuthorize("hasAuthority('MANAGER_ACCESS')")
    @GetMapping(ACTIVE_USERS)
    public ResponseEntity<?> getActiveUsers(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(service.activeUsersStatistics(httpServletRequest));
    }

}
