package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DailyReportResponse {

    private List<ProductRevenue> soldProducts;

    private List<DailyBoughtItem> boughtItems;

    private BigDecimal totalRevenue;

    private BigDecimal totalCashReceive;

    private BigDecimal totalExpense;
}
