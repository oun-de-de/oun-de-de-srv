package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class MonthlyReportResponse {

    private BigDecimal accountsReceivable;

    private BigDecimal saleInvoice;

    private BigDecimal cashInstallment;

    private List<MonthlyExpenseLine> expenses;
}
