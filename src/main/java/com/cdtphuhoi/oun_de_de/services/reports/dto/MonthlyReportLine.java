package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MonthlyReportLine {

    private LocalDateTime date;

    private String refNo;

    private String reason;

    private String customerName;

    private String memo;

    private BigDecimal debit;

    private BigDecimal credit;

    private BigDecimal balance;
}
