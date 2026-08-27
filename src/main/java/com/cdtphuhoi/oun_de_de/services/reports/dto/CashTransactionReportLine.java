package com.cdtphuhoi.oun_de_de.services.reports.dto;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CashTransactionReportLine {

    private int no;

    private LocalDateTime date;

    private String refNo;

    private CashTransactionType type;

    private String name;

    private String memo;

    private BigDecimal debit;

    private BigDecimal credit;

    private BigDecimal balance;
}
