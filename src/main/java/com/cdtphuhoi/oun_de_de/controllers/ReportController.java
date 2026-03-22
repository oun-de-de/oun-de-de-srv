package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.services.reports.ReportingService;
import com.cdtphuhoi.oun_de_de.services.reports.dto.DailyReportResponse;
import com.cdtphuhoi.oun_de_de.services.reports.dto.InventoryStockReportLine;
import com.cdtphuhoi.oun_de_de.services.reports.dto.MonthlyReportResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import jakarta.validation.constraints.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportingService reportingService;

    @GetMapping("/daily-report")
    public ResponseEntity<DailyReportResponse> getDailyReport(
        @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(
            reportingService.getDailyReport(date)
        );
    }

    @GetMapping("/monthly-report")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
        @RequestParam(required = true)
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "period must be in format YYYY-MM")
        String period
    ) {
        var yearMonth = YearMonth.parse(period);
        return ResponseEntity.ok(
            reportingService.getMonthlyReport(yearMonth)
        );
    }


    @GetMapping("/inventory-stock-report")
    public ResponseEntity<List<InventoryStockReportLine>> getInventoryStockReport(
        @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return ResponseEntity.ok(
            reportingService.getInventoryStockReport(fromDate, toDate)
        );
    }
}
