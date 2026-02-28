package com.cdtphuhoi.oun_de_de.services.dashboard;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import com.cdtphuhoi.oun_de_de.common.LoanInstallmentStatus;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.repositories.LoanInstallmentRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.DailyReportResponse;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.FinancialOverviewResponse;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.GetPerformanceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DashboardService implements OrgManagementService {

    private final PaymentTermCycleRepository paymentTermCycleRepository;

    private final LoanInstallmentRepository loanInstallmentRepository;

    private final PaymentRepository paymentRepository;

    public FinancialOverviewResponse getFinancialOverview(String orgId) {
        return FinancialOverviewResponse.builder()
            .invoiceAmount(paymentTermCycleRepository.sumAmount(orgId))
            .overdueCycles(paymentTermCycleRepository.countByStatus(PaymentTermCycleStatus.OVERDUE))
            .overdueLoanInstallments(loanInstallmentRepository.countByStatus(LoanInstallmentStatus.OVERDUE))
            .depositBalance(BigDecimal.ZERO)
            .build();
    }

    public GetPerformanceResponse getPerformance(String orgId) {
        return GetPerformanceResponse.builder()
            .expenses(BigDecimal.ZERO)
            .income(paymentTermCycleRepository.sumPaidAmount(orgId))
            .build();
    }

    public List<DailyReportResponse> getDailyReport(int range, String orgId) {
        var toDate = cambodiaNow();
        var fromDate = startOfDayInCambodia(toDate.minusDays(range));
        return paymentRepository.sumAmountByDateRange(fromDate, toDate, orgId)
            .stream()
            .map(
                row -> DailyReportResponse.builder()
                    .date((Date) row[0])
                    .income((BigDecimal) row[1])
                    .expense(BigDecimal.ZERO)
                    .build()
            )
            .toList();
    }
}
