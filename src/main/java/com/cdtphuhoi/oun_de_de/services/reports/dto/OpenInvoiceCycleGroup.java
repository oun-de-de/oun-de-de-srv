package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OpenInvoiceCycleGroup {

    private LocalDateTime cycleStartDate;

    private LocalDateTime cycleEndDate;

    private BigDecimal totalOriginalAmount;

    private BigDecimal totalPaidAmount;

    private BigDecimal balance;

    private List<OpenInvoiceReportLine> invoices;
}
