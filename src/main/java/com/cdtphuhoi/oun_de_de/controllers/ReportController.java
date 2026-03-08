package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import com.cdtphuhoi.oun_de_de.services.reports.ReportingService;
import com.cdtphuhoi.oun_de_de.services.reports.dto.DailyReportResponse;
import com.cdtphuhoi.oun_de_de.utils.SecurityContextUtils;
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
        var orgId = SecurityContextUtils.getCurrentUserProperty(UserDetailsImpl::getOrgId);
        return ResponseEntity.ok(
            reportingService.getReport(date, orgId)
        );
    }
}
