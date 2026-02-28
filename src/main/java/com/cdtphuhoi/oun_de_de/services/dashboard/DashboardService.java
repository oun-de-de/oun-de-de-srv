package com.cdtphuhoi.oun_de_de.services.dashboard;

import com.cdtphuhoi.oun_de_de.common.LoanInstallmentStatus;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.repositories.LoanInstallmentRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.FinancialOverviewResponse;
import com.cdtphuhoi.oun_de_de.services.dashboard.dto.GetPerformanceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DashboardService implements OrgManagementService {

    private final PaymentTermCycleRepository paymentTermCycleRepository;

    private final LoanInstallmentRepository loanInstallmentRepository;

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
}
