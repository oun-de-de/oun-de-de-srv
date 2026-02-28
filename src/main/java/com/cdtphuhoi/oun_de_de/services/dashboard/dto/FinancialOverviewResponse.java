package com.cdtphuhoi.oun_de_de.services.dashboard.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class FinancialOverviewResponse {
    private BigDecimal invoiceAmount;

    private Long overdueCycles;

    private Long overdueLoanInstallments;

    // maybe use for later
    private BigDecimal depositBalance;
}
