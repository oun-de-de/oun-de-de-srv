package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CustomerInvoiceLine {

    private LocalDateTime date;

    private String refNo;

    private Integer term;

    private LocalDateTime startDate;

    private LocalDateTime dueDate;

    private BigDecimal amount;

    private BigDecimal total;

    private BigDecimal remaining;
}
