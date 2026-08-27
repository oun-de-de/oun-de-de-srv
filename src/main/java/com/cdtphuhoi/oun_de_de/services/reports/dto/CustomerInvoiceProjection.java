package com.cdtphuhoi.oun_de_de.services.reports.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerInvoiceProjection(
    LocalDateTime invoiceDate,
    String refNo,
    String customerName,
    LocalDateTime cycleStartDate,
    LocalDateTime cycleEndDate,
    BigDecimal cycleTotal,
    BigDecimal cyclePaid,
    Integer term,
    BigDecimal originalAmount
) {}
