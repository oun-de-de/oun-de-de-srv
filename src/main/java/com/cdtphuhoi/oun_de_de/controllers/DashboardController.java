package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import com.cdtphuhoi.oun_de_de.services.dashboard.DashboardService;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.DailyReportResponse;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.FinancialOverviewResponse;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.GetPerformanceResponse;
import com.cdtphuhoi.oun_de_de.utils.SecurityContextUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/financial-overview")
    public ResponseEntity<FinancialOverviewResponse> getFinancialOverview() {
        var orgId = SecurityContextUtils.getCurrentUserProperty(UserDetailsImpl::getOrgId);
        return ResponseEntity.ok(
            dashboardService.getFinancialOverview(orgId)
        );
    }

    @GetMapping("performance")
    public ResponseEntity<GetPerformanceResponse> getPerformance() {
        var orgId = SecurityContextUtils.getCurrentUserProperty(UserDetailsImpl::getOrgId);
        return ResponseEntity.ok(
            dashboardService.getPerformance(orgId)
        );
    }

    @GetMapping("/daily-report")
    public ResponseEntity<List<DailyReportResponse>> getDailyReport(
        @RequestParam(required = true) Integer range
    ) {
        var orgId = SecurityContextUtils.getCurrentUserProperty(UserDetailsImpl::getOrgId);
        return ResponseEntity.ok(
            dashboardService.getDailyReport(range, orgId)
        );
    }
}
